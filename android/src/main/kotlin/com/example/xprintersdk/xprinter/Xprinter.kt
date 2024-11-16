package com.example.xprintersdk.xprinter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.IBinder
import android.util.Log
import io.flutter.plugin.common.MethodChannel
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.TaskCallback
import net.posprinter.service.PosprinterService
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.DataForSendToPrinterPos80
import net.posprinter.utils.PosPrinterDev

class Xprinter(mcontext : Context) {
    private var context : Context;
    init {
        context = mcontext
    }
    var binder: IMyBinder? = null
    var conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            binder = iBinder as IMyBinder
            Log.e("binder", "connected")
        }
        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e("disbinder", "disconnected")
        }
    }

    fun initBinding() {
        val posService = Intent(context, PosprinterService::class.java)
        context.bindService(posService, conn, Context.BIND_AUTO_CREATE)
    }

    fun disposeBinding(result: MethodChannel.Result) {
        binder!!.DisconnectCurrentPort(object : TaskCallback {
            override fun OnSucceed() {
                result.success(true);
            }
            override fun OnFailed() {
                result.success(false);
            }
        })
    }

    fun checkConnection(result: MethodChannel.Result) {
        binder!!.CheckLinkedState(object : TaskCallback {
            override fun OnSucceed() {
                result.success(true);
            }
            override fun OnFailed() {
                result.success(false);
            }
        })
    }

    fun connectNet(ipAddress: String?, result: MethodChannel.Result) {
        if (ipAddress == "") {
            result.success(false);
        } else {
            if (binder != null) {
                binder!!.ConnectNetPort(ipAddress, 9100, object : TaskCallback {
                    override fun OnSucceed() {
                        result.success(true);
                    }
                    override fun OnFailed() {
                        result.success(false);
                    }
                })
            } else {
                result.success(false);
            }
        }
    }

    fun connetUSB(result: MethodChannel.Result) {
        val usbList = PosPrinterDev.GetUsbPathNames(context)
        Log.d("usblist", "connetUSB: $usbList")
        if (usbList != null && usbList.size > 0) {
            if (binder != null) {
                binder!!.ConnectUsbPort(context,usbList[0], object : TaskCallback {
                    override fun OnSucceed() {
                        result.success(true);
                    }
                    override fun OnFailed() {
                        result.success(false);
                    }
                })
            } else {
                result.success(false);
            }
        } else {
            result.success(false);
        }
    }

    private fun cutBitmap(h: Int, bitmap: Bitmap?): List<Bitmap?> {
        val width = bitmap!!.width
        val height = bitmap.height
        val full = height % h == 0
        val n = if (height % h == 0) height / h else height / h + 1
        var b: Bitmap?
        val bitmaps: MutableList<Bitmap?> = ArrayList()
        for (i in 0 until n) {
            b = if (full) {
                Bitmap.createBitmap(bitmap, 0, i * h, width, h)
            } else {
                if (i == n - 1) {
                    Bitmap.createBitmap(bitmap, 0, i * h, width, height - i * h)
                } else {
                    Bitmap.createBitmap(bitmap, 0, i * h, width, h)
                }
            }
            bitmaps.add(b)
        }
        return bitmaps
    }


    fun printUSBbitamp(printBmp: Bitmap?, result: MethodChannel.Result) {
        val height = printBmp!!.height
        // if height > 200 cut the bitmap
        if (height > 200) {
            binder!!.WriteSendData(object : TaskCallback {
                override fun OnSucceed() {
                  result.success(true);
                }

                override fun OnFailed() {
                    result.success(false);
                }
            }) {
                val list: MutableList<ByteArray> =
                    ArrayList()
                list.add(DataForSendToPrinterPos80.initializePrinter())
                var bitmaplist: List<Bitmap?> =
                    ArrayList()
                bitmaplist = cutBitmap(200, printBmp) //cut bitmap
                if (bitmaplist.isNotEmpty()) {
                    for (i in bitmaplist.indices) {
                        list.add(
                            DataForSendToPrinterPos80.printRasterBmp(
                                0,
                                bitmaplist[i],
                                BitmapToByteData.BmpType.Threshold,
                                BitmapToByteData.AlignType.Center,
                                576
                            )
                        )
                    }
                }
                list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                list
            }
        } else {
            binder!!.WriteSendData(object : TaskCallback {
                override fun OnSucceed() {
                    result.success(true);
                }

                override fun OnFailed() {
                    result.success(false);
                }
            }) {
                val list: MutableList<ByteArray> =
                    ArrayList()
                list.add(DataForSendToPrinterPos80.initializePrinter())
                list.add(
                    DataForSendToPrinterPos80.printRasterBmp(
                        0,
                        printBmp,
                        BitmapToByteData.BmpType.Threshold,
                        BitmapToByteData.AlignType.Center,
                        600
                    )
                )
                list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                list
            }
        }
    }


}