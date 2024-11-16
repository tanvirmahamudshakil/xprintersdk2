package com.example.xprintersdk.PrinterService

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.xprintersdk.Model.BookingRequest.BookingRequest
import com.example.xprintersdk.Model.BusinessModel.BusinessSetting
import com.example.xprintersdk.Model.ReturnModel.ReturnModel
import com.example.xprintersdk.Nyxprinter.NyxprinterHelp
import com.example.xprintersdk.Sunmi.SunmiHelp
import com.example.xprintersdk.databinding.BookingrequestuiBinding
import com.example.xprintersdk.databinding.ReturnPrintBinding
import com.example.xprintersdk.xprinter.Xprinter
import io.flutter.plugin.common.MethodChannel
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class ReturnPrint(mcontext: Context, returnprint: ReturnModel, businessdata: BusinessSetting, mserviceBinding: Xprinter, mresult: MethodChannel.Result, sunmiHelper : SunmiHelp, saveImage: Boolean, nyxp : NyxprinterHelp) : AsyncTask<String, Int, Bitmap>() {

    private var context: Context
    private  var propertyrReturnprint: ReturnModel
    private  var businessname: String
    private  var businessaddress: String
    private  var businessphone: String
    private var fontsize: Int = 30
    private var noofprint: Int =1
    private var businessdatadata: BusinessSetting
    private var serviceBinding: Xprinter
    private var result: MethodChannel.Result
    private var sunmiPrinter : SunmiHelp
    private var bitmapSave: Boolean
    private var nyxprinter: NyxprinterHelp
    init {
        context = mcontext;
        propertyrReturnprint = returnprint;
        serviceBinding = mserviceBinding;
        this.businessname = businessdata.businessname!!;
        this.businessaddress =  businessdata.businessaddress!!;
        this.businessphone =  businessdata.businessphone!!;
        this.fontsize =  businessdata.fontSize!!;
        noofprint = businessdata.printOnCollection!!;
        businessdatadata = businessdata
        result = mresult
        sunmiPrinter = sunmiHelper;
        bitmapSave = saveImage;
        nyxprinter = nyxp
    }

    private fun getBitmapFromView(view: View): Bitmap {
        var bitmaplist : ArrayList<Bitmap>  = ArrayList<Bitmap>();
        val spec = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        view.measure(spec, spec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)

        //create resized image and display
//        val maxImageSize = 570f
//        val ratio = maxImageSize / returnedBitmap.width
//        val width = (ratio * returnedBitmap.width).roundToInt()
//        val height = (ratio * returnedBitmap.height).roundToInt()
//        //return the bitmap
//
//        var bitmap = Bitmap.createScaledBitmap(returnedBitmap, width, height, true)
        var bitmap: Bitmap = if (businessdatadata.paperSize == 80){
            //create resized image and display
            val maxImageSize = 570f
            val ratio = maxImageSize / returnedBitmap.width
            val width = (ratio * returnedBitmap.width).roundToInt()
            val height = (ratio * returnedBitmap.height).roundToInt()
            Bitmap.createScaledBitmap(returnedBitmap, width, height, true)
        }else {
            val maxImageSize = 390f
            val ratio = maxImageSize / (returnedBitmap.width)
            val width = (ratio * returnedBitmap.width).roundToInt()
            val height = (ratio * returnedBitmap.height).roundToInt()
            Bitmap.createScaledBitmap(returnedBitmap, width, height, true)
        }

        return bitmap;
    }

    fun printBitmap(bitmap: Bitmap?)  {
        try {
//            val originalBitmap: Bitmap? = bitmap
//            val compressFormat = Bitmap.CompressFormat.JPEG
//            val compressionQuality = 10 // Adjust the quality as needed
//            val compressedData = originalBitmap?.let { compressBitmap(it, compressFormat, compressionQuality) }
//
//            var b2 = resizeImage(byteArrayToBitmap(compressedData!!), 550, true)
            if(bitmapSave) {
                saveBitmapToGallery(context, bitmap!!, "bitmapImage", "scascas");
            }else if (businessdatadata.selectPrinter!!.lowercase() == "xprinter"){
                serviceBinding.printUSBbitamp(bitmap,result);
            } else if (businessdatadata.selectPrinter!!.lowercase() == "nyxprinter") {
                nyxprinter.printBitmap(bitmap!!, result)
            } else{
                sunmiPrinter.printBitmap(bitmap, 2, result)
            }


        } catch (e: java.lang.Exception) {

        }
    }

    private fun resizeImage(bitmap: Bitmap?, w: Int, ischecked: Boolean): Bitmap? {
        var resizedBitmap: Bitmap? = null
        val width = bitmap!!.width
        val height = bitmap.height
        if (width == w) {
            return bitmap
        }

        val newHeight = height * w / width
        val scaleWidth = w.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        resizedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            width,
            height,
            matrix,
            true
        )
        return resizedBitmap
    }


    fun compressBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(format, quality, stream)
        return stream.toByteArray()
    }


    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String, description: String) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, title)
            put(MediaStore.Images.Media.DESCRIPTION, description)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        try {
            if (uri != null) {
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()
                    Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun doInBackground(vararg params: String?): Bitmap {
        var binding = ReturnPrintBinding.inflate(LayoutInflater.from(context))
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        binding.nameValue.text = propertyrReturnprint.name
        binding.nameValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.fontSize?.toFloat() ?: 16f)
        binding.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.fontSize?.toFloat() ?: 16f)
        binding.quantityValue.text = propertyrReturnprint.quantity.toString()
        binding.quantityValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.fontSize?.toFloat() ?: 16f)
        binding.quantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.fontSize?.toFloat() ?: 16f)

        binding.dateBox.text = "${formatter.format(parser.parse(propertyrReturnprint.date))}";
        binding.dateBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.fontSize?.toFloat() ?: 16f)
        binding.arrivaltime.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.fontSize?.toFloat() ?: 16f)

        val bitmaplist: Bitmap =  getBitmapFromView(binding.root)
        return  bitmaplist;

    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        printBitmap(result)
    }
}