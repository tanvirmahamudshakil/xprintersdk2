package com.example.xprintersdk.PrinterService

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.xprintersdk.LabelPrinter.LabelPrinter
import com.example.xprintersdk.Model.BusinessModel.BusinessSetting
import com.example.xprintersdk.Model.OrderData.OrderData
import com.example.xprintersdk.Nyxprinter.NyxprinterHelp
import com.example.xprintersdk.Printer80.printer80
import com.example.xprintersdk.Sunmi.SunmiHelp
import com.example.xprintersdk.databinding.ButcherOrderPrintBinding
import com.example.xprintersdk.databinding.ModelPrint2Binding
import com.example.xprintersdk.databinding.OnlinePrint2Binding
import com.example.xprintersdk.databinding.StickerprinterBinding
import com.example.xprintersdk.xprinter.Xprinter
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import io.flutter.plugin.common.MethodChannel
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


class printerservice(mcontext: Context, morderModel: OrderData, businessdata: BusinessSetting, mserviceBinding: Xprinter, mresult: MethodChannel.Result, sunmiHelper : SunmiHelp, saveImage: Boolean, nyxp : NyxprinterHelp, labelPrinter : LabelPrinter, printer80D : printer80) :
    AsyncTask<String, Int, Bitmap>()
     {

         private var context: Context
         private  var orderModel: OrderData
         private  var businessname: String
         private  var businessaddress: String
         private  var businessphone: String
         private var fontsize: Int = 30
         private var labelPrinter: LabelPrinter
         private var printer80: printer80
         private var noofprint: Int =1
         private var businessdatadata: BusinessSetting
         private var serviceBinding: Xprinter
         private var result: MethodChannel.Result
         private var sunmiPrinter : SunmiHelp
         private var bitmapSave: Boolean
         private var nyxprinter : NyxprinterHelp
         private var header1 : Int = 22
         private var header2 : Int = 22
         private var header3 : Int = 22
         private var header4 : Int = 22

         private var footervatFontSize : Int = 15
         lateinit var barcode: String
         private val size_width = 660
         private val size_height = 264

    init {
        context = mcontext;
        orderModel = morderModel;
        printer80 = printer80D
        serviceBinding = mserviceBinding;
        this.businessname = businessdata.businessname ?: "";
        this.businessaddress =  businessdata.businessaddress ?: "";
        this.businessphone =  businessdata.businessphone ?: "";
        this.fontsize =  businessdata.fontSize ?: 30;
        noofprint = businessdata.printOnCollection ?: 1;
        businessdatadata = businessdata
        result = mresult
        sunmiPrinter = sunmiHelper;
        this.labelPrinter = labelPrinter
        bitmapSave = saveImage;
        header1 = businessdata.header1Size ?: 22;
        header2 = businessdata.header2Size ?: 22;
        header3 = businessdata.header3Size ?: 22;
        header4 = businessdata.header4Size ?: 22;
        footervatFontSize = businessdata.footervatFontSize ?: 12
        this.nyxprinter = nyxp

    }


         var x_for_poundOfferApplyList  = mutableListOf<Int>();


         fun rotateBitmap180(bitmap: Bitmap): Bitmap {
             val matrix = Matrix().apply {
                 postRotate(180f)
             }
             return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
         }


    fun capitalize(str: String): String? {
        return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
         private fun getBitmapFromView(view: View): Bitmap {

             if(businessdatadata.selectPrinter!!.lowercase() == "label_printer") {

                 val dpi = businessdatadata.dpi ?: 203
                 val widthMm = businessdatadata.label_width ?: 76
                 val heightMm = businessdatadata.label_hight ?: 76

// Convert mm to pixels
                 val widthPx = (widthMm * dpi / 25.4f).toInt()
                 val heightPx = (heightMm * dpi / 25.4f).toInt()

// Measure and layout the view to the desired size
                 val specWidth = View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY)
                 val specHeight = View.MeasureSpec.makeMeasureSpec(heightPx, View.MeasureSpec.EXACTLY)
                 view.measure(specWidth, specHeight)
                 view.layout(0, 0, widthPx, heightPx)

// Create the bitmap with specified dimensions
                 val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
                 val canvas = Canvas(bitmap)

// Optional: center the view within the canvas
                 val offsetX = (widthPx - view.measuredWidth) / 2
                 val offsetY = (heightPx - view.measuredHeight) / 2
                 canvas.translate(offsetX.toFloat(), offsetY.toFloat())

// Draw the view's background if it exists
                 view.background?.draw(canvas) ?: canvas.drawColor(Color.WHITE)

// Draw the view onto the canvas
                 view.draw(canvas)

                 return rotateBitmap180(bitmap)

             }else{
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


                 var bitmap: Bitmap = if (businessdatadata.paperSize == 80) {
                     //create resized image and display
                     val maxImageSize = 570f
                     val ratio = maxImageSize / returnedBitmap.width
                     val width = (ratio * returnedBitmap.width).roundToInt()
                     val height = (ratio * returnedBitmap.height).roundToInt()
                     Bitmap.createScaledBitmap(returnedBitmap, width, height, true)
                 } else {
                     val maxImageSize = 390f
                     val ratio = maxImageSize / (returnedBitmap.width)
                     val width = (ratio * returnedBitmap.width).roundToInt()
                     val height = (ratio * returnedBitmap.height).roundToInt()
                     Bitmap.createScaledBitmap(returnedBitmap, width, height, true)
                 }
                 return bitmap;
             }














    }
         fun getResizedBitmap(originalBitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
             val ratioX = targetWidth.toFloat() / originalBitmap.width
             val ratioY = targetHeight.toFloat() / originalBitmap.height
             val ratio = minOf(ratioX, ratioY)
             val width = (ratio * originalBitmap.width).toInt()
             val height = (ratio * originalBitmap.height).toInt()

             return Bitmap.createScaledBitmap(originalBitmap, width, height, true)
         }

         fun componentFilter( i: OrderData.OrderProduct.Component?) : Boolean {
             return if(i?.product?.type?.uppercase() == "COMPONENT") {
//                 if (i.product.property != null){
//                     if( i.product.property.itemtype != null) {
//                         !(((i.product.property.itemtype?.lowercase() == "topping") || (i.product.property.itemtype?.lowercase() == "addon") || (i.product.property.itemtype?.lowercase() == "dressing")))
//                     }else{
//                         true;
//                     }
//                 }else{
//                     true;
//                 }
                 true;
             }else{
                 false;
             }
         }

          fun getView( listorderProducts: List<OrderData.OrderProduct?>?, item: OrderData.OrderProduct?, iteamLength : Int ,position: Int, mCtx: Context?, style: Int, fontSize: Int): View? {
        val binding: ModelPrint2Binding = ModelPrint2Binding.inflate(LayoutInflater.from(mCtx))
        var  component: List<OrderData.OrderProduct.Component?>?
        var  extraIteam: List<OrderData.OrderProduct.Component?>? = ArrayList()
        if(orderModel.orderChannel?.uppercase() == "ONLINE") {
            component = item?.components;
        } else {
             component = item?.components?.filter {i-> componentFilter(i)}
          //   extraIteam = item?.components?.filter { i-> i?.product?.property?.itemtype != null && (i.product.property.itemtype?.lowercase() == "topping" || i.product.property.itemtype?.lowercase() == "addon" || i.product.property.itemtype?.lowercase() == "dressing")}
            extraIteam = item?.components?.filter { i-> i?.product?.type == "EXTRA-COMPONENT"}

        }


        val str3 = StringBuilder()
        var price = 0.0
        var tareWeight : Double = if(item?.product?.property?.tare_weight?.isEmpty() == true) {
                   0.0;
              }else{
                   item?.product?.property?.tare_weight?.toDouble() ?: 0.0
              }
        var unitAmount = if(item?.product?.property?.unit_amount?.isEmpty() == true) {
             0.0
        } else{
            item?.product?.property?.unit_amount?.toDouble() ?: 0.0
        }


        if(unitAmount == 0.0) {
            binding.unitValue.visibility = View.GONE
        }else{
            if(businessdatadata.weightShow) {
                binding.unitValue.visibility = View.VISIBLE
                binding.unitValue.text = "${unitAmount} ${unitGet(item)}"
                binding.unitValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, (header3.toFloat() - 5))
            } else {
                binding.unitValue.visibility = View.GONE
            }

        }
        if(item?.product?.property?.unit_product_type?.uppercase() == "WEIGHT") {
            if(businessdatadata.weightMultiplyingPrice) {
                price = (item.netAmount ?: 0.0) * ((if (unitAmount == 0.0) 1.0 else unitAmount) - tareWeight)
            }else{
                price = (item.netAmount ?: 0.0)
            }

        }else{
            price = (item?.netAmount ?: 0.0)
        }

        var discount = item?.discountableAmount ?: 0.0;
        if (position < iteamLength - 1) {
            if ((listorderProducts!![position]?.product?.property?.printorder?.toInt()
                            ?: 0) < (listorderProducts[position + 1]?.product?.property?.printorder?.toInt()
                            ?: 0)) {

                binding.underLine.visibility = View.VISIBLE
            }

        }

        if (!component.isNullOrEmpty()) {
            str3.append(item?.unit).append("x ").append(item?.product?.shortName)
            for (section in component) {
                var _comName = ""
                if (section?.product?.shortName?.uppercase() != "NONE") {
                    _comName = section?.product?.shortName ?: ""
                }
                if ((section?.components != null) && section.components.isNotEmpty()) {
                    for (section2 in section.components) {
                        if (section2?.product?.shortName?.uppercase() != "NONE") {
                            _comName += " -> " + "${section2?.unit ?: 1}x " + section2?.product?.shortName;
                            price += ((section2?.netAmount ?: 0.0) * (section2?.unit ?: 1));
                        }
                    }

                }
                if (_comName != "") {
                    if(businessdatadata.printerStyle == "1") {
                        str3.append("\n").append(_comName)
                    }else{
                        str3.append(" -> ").append(_comName)
                    }

                }
                price += section?.netAmount ?: 0.0;
            }
        } else {
            if (item?.product?.type == "ITEM" || item?.product?.type == "DYNAMIC"){
                str3.append(item.unit).append("x ").append(item.product.shortName)
                if(businessdatadata.printerStyle == "2"){
                    if(item.product.property?.printorder == "2"){
                        str3.append("(Str)")
                    }
                }
            }

        }

        if (extraIteam != null) {
            val topping = StringBuilder()
            if (extraIteam.isNotEmpty()) {
                if(businessdatadata.printerStyle == "1") {
                    topping.append("\n")
                }else{
                    topping.append(" -> ")
                }
//                val topping = java.lang.StringBuilder("\n")
                for (extraItem in extraIteam) {
                    topping.append("  *").append(extraItem?.product?.shortName)
                    price += extraItem?.netAmount!!;
                }
                str3.append(topping.toString())
            }
        }


        if(orderModel.orderChannel?.uppercase() != "ONLINE"){
//            price *= (item?.unit ?: 1)
            if (item?.offer?.offer?.type == "X_FOR_Y" && item?.offer?.offer?.status == 1) {
                var p = String.format("%.2f", getOrderOfferPrice(item))
                price *=  p.toDouble()
            }else if (item?.offer?.offer?.type == "X_FOR_£" && item?.offer?.offer?.status == 1) {
                var p = String.format("%.2f", xForPoundOfferLocalDetailOrder(item, listorderProducts))
                Log.e("price get", "getView: ${p}----")
                price =  p.toDouble()
            }else{
                price *= (item?.unit ?: 1)
            }

            var totaldiscount = (price * (discount / 100))
            price -= totaldiscount;
        }
        Log.e("price get", "getView: ${price}----")
        if(item?.comment != null && (item.product?.type == "ITEM" || item.product?.type == "DYNAMIC")) str3.append("\nNote : ").append(item.comment)
        binding.itemText.text = str3.toString()
        binding.itemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, header3.toFloat())
        if(item?.product?.type == "ITEM" || item?.product?.type == "DYNAMIC"){
           binding.itemPrice.text = "£ ${String.format("%.2f", price)}"
        } else{
           binding.itemPrice.visibility = View.GONE
        }
        binding.itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, header3.toFloat())
        binding.root.buildDrawingCache(true)
        return binding.root
    }

         fun unitGet(data: OrderData.OrderProduct?): String {
             val unitOfSale = data?.product?.property?.unit_of_sale
             return when (unitOfSale) {
                 "POUND" -> "P"
                 "KG" -> "KG"
                 "PCS" -> "PCS"
                 "TON" -> "TON"
                 "LB" -> "LB"
                 else -> "G"
             }
         }

         fun getOrderOfferPrice(data: OrderData.OrderProduct): Int {
             val totalItems = data.unit ?: 1
             if (data.offer?.offer?.type == "X_FOR_Y") {
                 val buyX : Int = data.offer.offer.buy ?: 0
                 val payY : Double = data.offer.offer.offerFor ?: 0.0
                 val groupSize = buyX
                 val fullPriceItems = totalItems % groupSize // Items not fitting into the promotion
                 val discountedGroups = totalItems / groupSize // Number of groups that fit into the promotion
                 // Total paid items for groups that fit the promotion plus any remaining full price items
                 val paidItems = discountedGroups * payY + fullPriceItems
                 return paidItems.toInt()
             } else {
                 return totalItems
             }
         }

         fun truncateToTwoDecimalPlaces(value: Double): Double {
             val factor = 100.0
             return kotlin.math.floor(value * factor) / factor
         }

         fun xForPoundOfferLocalDetailOrder(data: OrderData.OrderProduct, listorderProducts: List<OrderData.OrderProduct?>?): Double {
             var totalBuy = 0
             val findOffer = listorderProducts!!.filter { it?.offer?.offer?.type == "X_FOR_£" && it?.offer?.offer?.offerFor == data.offer?.offer?.offerFor }

             if (findOffer.isNotEmpty()) {
                 findOffer.forEach { e ->
                     totalBuy += e?.unit ?: 1
                 }

                 val buy : Int = data.offer?.offer?.buy ?: 0
                 val forPound : Double = data.offer?.offer?.offerFor ?: 0.0

                 if (totalBuy % buy == 0) {
                     val offerPrice = (forPound / buy) * (data.unit ?: 1)
                     x_for_poundOfferApplyList.add(data.unit ?: 1)
                     var of = String.format("%.2f", offerPrice.toFloat())
                     Log.e("offer 1", "getView: ${of}----${offerPrice.toFloat()}")
                     return truncateToTwoDecimalPlaces(offerPrice)
                 } else if (totalBuy > buy) {
                     val itemQuantity = data.unit ?: 1
                     if (itemQuantity == buy) {
                         val offerPrice = (forPound / buy) * itemQuantity
                         x_for_poundOfferApplyList.add(itemQuantity)
                         var of = String.format("%.2f", offerPrice)
                         Log.e("offer 2", "getView: ${of.toDouble()}----")
                         return truncateToTwoDecimalPlaces(offerPrice)
                     } else if (itemQuantity > buy) {
                         val otherQuantity = itemQuantity - buy
                         val offerQuantity = (data.unit ?: 1) - otherQuantity
                         val offerPrice = (forPound / buy) * offerQuantity
                         val fullPrice = (data.netAmount ?: 0.0) * otherQuantity
                         x_for_poundOfferApplyList.add(itemQuantity)
                         var of = String.format("%.2f", offerPrice)
                         Log.e("offer 3", "getView: ${of.toDouble()}----${fullPrice}")
                         return fullPrice + (truncateToTwoDecimalPlaces(offerPrice))
                     } else {
                         var totalQuantityApply = 0
                         if (findOffer.first()?.id == data.id) {
                             x_for_poundOfferApplyList.clear()
                         }
                         x_for_poundOfferApplyList.forEach { e ->
                             totalQuantityApply += e
                         }
                         println("sdbjshdbv $totalQuantityApply")
                         if (totalQuantityApply < buy) {
                             val available = buy - totalQuantityApply
                             if (available < itemQuantity) {
                                 val otherQuantity = itemQuantity - available
                                 val offerQuantity = available
                                 val offerPrice = (forPound / buy) * offerQuantity
                                 val fullPrice = (data.netAmount ?: 0.0) * otherQuantity
                                 x_for_poundOfferApplyList.add(itemQuantity)
                                 var of = String.format("%.2f", offerPrice)
                                 Log.e("offer 4", "getView: ${of.toDouble()}----${fullPrice}")
                                 return fullPrice + truncateToTwoDecimalPlaces(offerPrice)
                             } else {
                                 val offerPrice = (forPound / buy) * itemQuantity
                                 x_for_poundOfferApplyList.add(itemQuantity)
                                 var of = String.format("%.2f", offerPrice)
                                 Log.e("offer 5", "getView: ${of.toDouble()}----")
                                 return truncateToTwoDecimalPlaces(offerPrice)
                             }
                         } else if (totalQuantityApply == buy) {
                             val fullPrice = (data.netAmount ?: 0.0) * itemQuantity
                             Log.e("offer 6", "getView: ${fullPrice}----")
                             return fullPrice;
                         } else {
                             x_for_poundOfferApplyList.remove(itemQuantity)
                             return (data.netAmount ?: 0.0) * (data.unit ?: 1)
                         }
                     }
                 } else {
                     x_for_poundOfferApplyList.remove(data.unit ?: 1)
                     return (data.netAmount ?: 0.0) * (data.unit ?: 1)
                 }
             }
             x_for_poundOfferApplyList.remove(data.unit ?: 1)
             return (data.netAmount ?: 0.0) * (data.unit ?: 1)
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
            } else if (businessdatadata.selectPrinter!!.lowercase() == "label_printer") {
                val dpi = businessdatadata.dpi ?: 203
                val widthMm = businessdatadata.label_width ?: 76

                val widthPx = (widthMm * dpi / 25.4f).toInt()
                 labelPrinter.printPicCode(
                     bitmap!!,
                     result,
                     (businessdatadata.label_width?.toDouble() ?: 76.0),
                     (businessdatadata.label_hight?.toDouble() ?: 30.0),
                     widthPx
                 )
            } else if (businessdatadata.selectPrinter!!.lowercase() == "printer80") {
                if (bitmap != null) {
                    printer80.printBitmap(bitmap)
                    result.success(true)
                };
            }
            else {
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


         fun getMinutesDifference(date1: String, date2: String): Long {
             val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

             try {
                 val parsedDate1: Date = dateFormat.parse("$date1 00:00:00")
                 val parsedDate2: Date = dateFormat.parse("$date2 00:00:00")

                 // Calculate the difference in milliseconds
                 val diffInMillis: Long = parsedDate2.time - parsedDate1.time

                 // Convert milliseconds to minutes
                 val minutesDifference: Long = diffInMillis / (1000 * 60)

                 return minutesDifference
             } catch (e: Exception) {
                 e.printStackTrace()
                 return -1 // Handle the exception according to your requirements
             }
         }

         fun  getOrderType(): String {
             if(orderModel.orderType == "COLLECTION") {
                 return "${businessdatadata.dynamicCollection?.uppercase()}";
             }else if (orderModel.orderType == "DELIVERY") {
                 return "${businessdatadata.dynamicDelivery?.uppercase()}";
             }else if(orderModel.orderType == "EAT IN") {
                 return "${businessdatadata.dynamicEatIn?.uppercase()}";
             }else if (orderModel.orderType == "TAKEAWAY") {
                 return "${businessdatadata.dynamicTakeaway?.uppercase()}";
             }else{
                 return "${orderModel.orderType}";
             }
         }

         private fun genBarcode(barcode : String) : Bitmap? {
             var widthd = businessdatadata.barcode_width ?: 250;
             var heightd = businessdatadata.barcode_hight ?: 100;

             var bitMatrix: BitMatrix? = null
             bitMatrix = MultiFormatWriter().encode(barcode, BarcodeFormat.CODE_128, widthd, heightd)
             val width = bitMatrix.width
             val height = bitMatrix.getHeight()
             val pixels = IntArray(width * height)
             for (i in 0 until height) {
                 for (j in 0 until width) {
                     if (bitMatrix[j, i]) {
                         pixels[i * width + j] = -0x1000000
                     } else {
                         pixels[i * width + j] = -0x1
                     }
                 }
             }
             val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
             bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
             return bitmap
//
//             val inputValue = barcode.trim()
//             var width = businessdatadata.barcode_width ?: 250;
//             var height = businessdatadata.barcode_hight ?: 100;
//             if (inputValue.isNotEmpty()) {
//                 val hints = mapOf(
//                     EncodeHintType.MARGIN to 0  // Optional: set margin to 0 for a tighter fit
//                 )
//                 // Initializing a MultiFormatWriter to encode the input value
//       //          val mwriter = MultiFormatWriter()
//                 try {
//                     val bitMatrix: BitMatrix = MultiFormatWriter().encode(
//                         inputValue,
//                         BarcodeFormat.CODE_128,  // Use CODE_128 or other formats as needed
//                         width,
//                         height,
//                         hints
//                     )
//
//                     val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//                     for (x in 0 until width) {
//                         for (y in 0 until height) {
//                             bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
//                         }
//                     }
//                     return bitmap
//                 } catch (e: Exception) {
//                    return  null;
//                 }
//             } else {
//                 // Showing an error message if the EditText is empty
//                return  null;
//             }
         }


         fun eposWaiterorderPrint() : Bitmap {
             noofprint = if (orderModel.orderType == "DELIVERY"){
                 businessdatadata.printOnDelivery!!
             }else{
                 businessdatadata.printOnCollection!!
             }

             val printSize: Int = fontsize
             val bind: OnlinePrint2Binding = OnlinePrint2Binding.inflate(LayoutInflater.from(context))
             bind.businessName.text = businessname
             bind.businessName.setTextSize(TypedValue.COMPLEX_UNIT_SP, header1.toFloat())

             val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
             val formatter = SimpleDateFormat("dd-MMM hh:mm a")
             val requestformatter = SimpleDateFormat("HH:mm")
             val formatter2 = SimpleDateFormat(" dd/MM hh:mm a")
             var totalRefund : Double = 0.0;
             var totalCardPaid : Double = 0.0;
             var totalCashPaid: Double = 0.0;
             var totalReceivePound : Double = 0.0
              // var totalDue : Double = 0.0;
             val refundList = orderModel.cashEntry?.filter { it?.type?.uppercase() == "REFUND"}
             var cardPaidList = orderModel.cashEntry?.filter { it?.type?.uppercase() == "EPOS_CARD" || it?.type?.uppercase() == "CARD"}
             var cashPaidList = orderModel.cashEntry?.filter { it?.type?.uppercase() == "EPOS_CASH" || it?.type?.uppercase() == "CASH"}

             val receivePoundList = orderModel.cashEntry?.filter { it?.type?.uppercase() != "REFUND"}
             val changeAmount : Double = orderModel.changeAmount ?: 0.0;
             refundList?.forEach {
                 totalRefund += (it?.amount ?: 0.0)
             }
             cardPaidList?.forEach {
                 totalCardPaid += (it?.amount?: 0.0)
             }
             cashPaidList?.forEach {
                 totalCashPaid += (it?.amount?: 0.0)
             }
             receivePoundList?.forEach {
                 totalReceivePound += (it?.amount ?: 0.0)
             }
             // totalDue = (orderModel.payableAmount ?: 0.0) - (totalReceivePound - (orderModel.changeAmount ?: 0.0))
             var addedDeliveryCharge = 0.0
             bind.businessLocation.text = businessaddress
             bind.businessLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, header1.toFloat())
             bind.businessPhone.text = businessphone
             bind.businessPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, header1.toFloat())
             bind.branchName.text = orderModel.branch?.name?.uppercase()
             bind.branchName.setTextSize(TypedValue.COMPLEX_UNIT_SP, header1.toFloat())
             if(orderModel.orderType == "TABLE_BOOKING") {
                 bind.orderType.text = "TABLE BOOKING #${orderModel.table_id}"
                 bind.orderType.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
             }else{
//                 bind.orderType.text =  getOrderType()
//                 bind.orderType.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                 if(orderModel.property?.requestedDeliveryTimestampType != null) {
                     if(businessdatadata.printerStyle == "3") {
                         bind.orderType.text =  "${getOrderType()} ${orderModel.property?.requestedDeliveryTimestampType}"
                         bind.orderType.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     }else{
                         bind.orderType.text =  getOrderType()
                         bind.orderType.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     }

                 }else{
                     if(businessdatadata.printerStyle == "3") {
                         bind.orderType.text =  "${getOrderType()}"
                         bind.orderType.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     }else{
                         bind.orderType.text =  getOrderType()
                         bind.orderType.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     }

                 }
326
             }
             bind.orderTime.text = "Order at : ${parser.parse(orderModel.orderDate)
                 ?.let { formatter.format(it) }}"
             bind.orderTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
             if(orderModel.orderType == "TABLE_BOOKING") {
                 bind.collectionAt.text = "TABLE BOOKING at : ${formatter.format(parser.parse(orderModel.requestedDeliveryTimestamp))}"
                 bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
             }else{
                 if(orderModel.property?.requestedDeliveryTimestampType != null) {
                     var asapdata = orderModel.property?.requestedDeliveryTimestampType;
                     if(businessdatadata.printerStyle == "3") {
                         bind.collectionAt.setTypeface(null, Typeface.BOLD)
                         bind.collectionAt.text = "REQUESTED at : ${requestformatter.format(parser.parse(orderModel.requestedDeliveryTimestamp))} ${asapdata}"
                         bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     }else{
                         bind.collectionAt.text = asapdata
                         bind.collectionAt.setTypeface(null, Typeface.BOLD)
                         bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.asapFontSize!!.toFloat())
                     }
//                     var asapdata = orderModel.property?.requestedDeliveryTimestampType;
//                     bind.collectionAt.text = asapdata
//                     bind.collectionAt.setTypeface(null, Typeface.BOLD)
//                     bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.asapFontSize?.toFloat() ?: 16f)

                 }else{
//                     bind.collectionAt.text = "REQUESTED at : ${formatter.format(parser.parse(orderModel.requestedDeliveryTimestamp))}"
//                     bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())

                     if(businessdatadata.printerStyle == "3") {
                         bind.collectionAt.setTypeface(null, Typeface.BOLD)
                         bind.collectionAt.text = "REQUESTED at : ${requestformatter.format(parser.parse(orderModel.requestedDeliveryTimestamp))}"
                         bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     }else{
                         bind.collectionAt.text = "REQUESTED at : ${formatter.format(parser.parse(orderModel.requestedDeliveryTimestamp))}"
                         bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     }
                 }

             }
             if ((orderModel.orderType?.uppercase() == "DELIVERY" || orderModel.orderType?.uppercase() == "COLLECTION") && getMinutesDifference(orderModel.orderDate
                     ?: "2024-01-22 07:48:10", orderModel.requestedDeliveryTimestamp
                     ?: "2024-01-22 17:20:00") >= (businessdatadata.highlight ?: 15)){
                 if(orderModel.property?.requestedDeliveryTimestampType == null) {
                     bind.collectionAt.setTypeface(null, Typeface.BOLD)
                     bind.collectionAt.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                     bind.collectionAt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, businessdatadata.highlighttextsize!!.toFloat())
                 }
             }
             if(orderModel.orderType == "TABLE_BOOKING") {
                 bind.orderText.text = "Table#"
                 bind.orderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
             }
             if(orderModel.orderChannel?.uppercase() == "ONLINE"){
                 bind.containerOrderNo.visibility = View.VISIBLE
                 bind.orderNo.text = "${orderModel.order_id}";
                 bind.orderNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                 bind.orderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
             }else{
                 if(orderModel.orderType == "TABLE_BOOKING"){
                     bind.containerOrderNo.visibility = View.GONE
//                     bind.orderNo.text = "${orderModel.table_id}";
                     bind.orderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())

                 }else{
                     bind.containerOrderNo.visibility = View.VISIBLE
                     bind.orderNo.text = "${orderModel.localId}";
                     bind.orderNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                     bind.orderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, header2.toFloat())
                 }
             }
             var allitemsheight = 0
             bind.items.removeAllViews()
             var itemproduict = orderModel.orderProducts?.filter { i-> i?.product?.type == "ITEM" || i?.product?.type == "DYNAMIC" }
             var sortIteam = itemproduict?.sortedWith(compareBy {it?.product?.property?.printorder?.toInt() ?: 0 })
             if(!sortIteam.isNullOrEmpty()){
                 for (j in sortIteam.indices) {
                     Log.e("iteam sort", "doInBackground: ${j}")
                     val childView = getView(sortIteam, sortIteam[j],sortIteam.size, j, context, 0, printSize)
                     bind.items.addView(childView)
                     allitemsheight += childView!!.measuredHeight
                 }

             }
             var paidOrNot = "";
             if (orderModel.orderChannel?.uppercase() == "ONLINE") {
                 if(totalRefund > 0.0) {
                     bind.RefundContainer.visibility = View.VISIBLE
                     bind.refund.text = "£ " + String.format("%.2f", totalRefund)
                 }else{
                     bind.RefundContainer.visibility = View.GONE
                 }

                 if(changeAmount > 0.0) {
                     bind.changeContainer.visibility = View.VISIBLE
                     bind.change.text = "£ " + String.format("%.2f", changeAmount)
                 }else{
                     bind.changeContainer.visibility = View.GONE

                 }
                 if(totalRefund > 0.0 || changeAmount > 0.0) {
                     bind.dottedBelowTotal.visibility = View.VISIBLE
                 }else{
                     bind.dottedBelowTotal.visibility = View.GONE
                 }

                 if(orderModel.status?.uppercase() == "REFUNDED") {
                     paidOrNot = "ORDER is REFUNDED"
                     bind.dueTotalContainer.visibility = View.VISIBLE
                     bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
                 }else{
                     if(orderModel.paymentType?.uppercase() == "CARD" || orderModel.paymentType?.uppercase() == "EPOS_CARD"){
                         paidOrNot ="ORDER IS PAID"
                     }else if(orderModel.paymentType?.uppercase() == "CASH" || orderModel.paymentType?.uppercase() == "EPOS_CASH") {
                         if (orderModel.cashEntry == null || orderModel.cashEntry!!.isEmpty()){
                             paidOrNot = "ORDER NOT PAID"
                             bind.dueTotalContainer.visibility = View.VISIBLE
                             bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
                         }else{
                             paidOrNot ="ORDER IS PAID"
                         }
                     }
                 }
             } else if (orderModel.orderChannel?.uppercase() != "ONLINE") {
                 if(totalRefund > 0.0) {
                     bind.RefundContainer.visibility = View.VISIBLE
                     bind.refund.text = "£ " + String.format("%.2f", totalRefund)
                 }else{
                     bind.RefundContainer.visibility = View.GONE
                 }

                 if(changeAmount > 0.0) {
                     bind.changeContainer.visibility = View.VISIBLE
                     bind.change.text = "£ " + String.format("%.2f", changeAmount)
                 }else{
                     bind.changeContainer.visibility = View.GONE
                 }
                 if(totalRefund > 0.0 || changeAmount > 0.0) {
                     bind.dottedBelowTotal.visibility = View.VISIBLE
                 }else{
                     bind.dottedBelowTotal.visibility = View.GONE
                 }

                 if(orderModel.status?.uppercase() == "REFUNDED") {
                     paidOrNot = "ORDER is REFUND"
                     bind.dueTotalContainer.visibility = View.VISIBLE

                     bind.dueTotal.text = "£ 0.0"

                 } else if(totalRefund > 0.0) {
                     paidOrNot = "ORDER is PARTIAL REFUND"
                     bind.dueTotalContainer.visibility = View.VISIBLE
                     bind.dueTotal.text = "£ 0.0"
                 } else{
                     if(orderModel.cashEntry != null && orderModel.cashEntry!!.isNotEmpty()) {
                         paidOrNot ="ORDER IS PAID"
                         bind.dueTotal.text = "£ 0.0"
                     }else{
                         if(orderModel.paymentType?.uppercase() == "UNPAID_CASH") {
                             paidOrNot ="ORDER IS UNPAID(CASH)"
                             bind.dueTotalContainer.visibility = View.VISIBLE
                             bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
                                 //"£ " + String.format("%.2f", orderModel.payableAmount)
                         }else if(orderModel.paymentType?.uppercase() == "UNPAID_CARD") {
                             paidOrNot ="ORDER IS UNPAID(CARD)"
                             bind.dueTotalContainer.visibility = View.VISIBLE
                             bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
                                 //"£ " + String.format("%.2f", orderModel.payableAmount)
                         }else{
                             paidOrNot = "ORDER NOT PAID"
                             bind.dueTotalContainer.visibility = View.VISIBLE
                             bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
                                 //"£ " + String.format("%.2f", orderModel.payableAmount)
                         }
                     }
                 }

             } else  {
                 paidOrNot = "ORDER NOT PAID"
                 bind.dueTotalContainer.visibility = View.VISIBLE
                 bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
                     //"£ " + String.format("%.2f", orderModel.payableAmount)
             }
//             else if (orderModel.orderChannel!!.uppercase() == "ONLINE" && orderModel.paymentType!!.uppercase() == "CASH") {
//                 if (orderModel.cashEntry!!.isEmpty()){
//                     paidOrNot = "ORDER NOT PAID"
//                     bind.dueTotalContainer.visibility = View.VISIBLE
//                     bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
//                 }else{
//                     paidOrNot ="ORDER IS PAID"
//                 }
//             }
             bind.orderPaidMessage.text = paidOrNot
             bind.orderPaidMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, header4.toFloat())
//             bind.refundContainer.visibility = View.GONE
             val subTotal: Double = orderModel.netAmount ?: 0.0
             bind.subTotal.text = "£ " + String.format( "%.2f", subTotal)
             if(orderModel.orderType == "DELIVERY") {
                 bind.deliveryChargeContainer.visibility = View.VISIBLE
                 bind.txtDeliveryCharge.text = "Delivery Charge";
                 bind.deliveryCharge.text = "£ " + orderModel.deliveryCharge!!.toFloat().toString()
             }else{
                 bind.deliveryChargeContainer.visibility = View.GONE
             }
             bind.change.text = "£ " +  String.format( "%.2f",  orderModel.changeAmount)
             if(totalCardPaid > 0) {
                 bind.cardPayContainer.visibility = View.VISIBLE
                 bind.cardPay.text = "£ " + String.format( "%.2f",  totalCardPaid)
             } else{
                 bind.cardPayContainer.visibility = View.GONE
             }
             if(totalCashPaid > 0) {
                 bind.cashPayContainer.visibility = View.VISIBLE
                 bind.cashPay.text = "£ " + String.format( "%.2f",  totalCashPaid)
             }else{
                 bind.cashPayContainer.visibility = View.GONE
             }


             if (orderModel.orderChannel?.uppercase() == "ONLINE") {
                 if ((orderModel.discountedAmount ?: 0.0) > 0) {
                     bind.discount.text =
                         "£ " +  String.format( "%.2f",  orderModel.discountedAmount)
                 } else bind.discount.text =
                     "£ " + String.format("%.2f", 0.0)
             } else {
                 var discountStr = "Discount"
                 bind.discountTitle.text = discountStr
                 var discountAmount = 0.0
                 bind.discount.text =
                     "£ " + String.format( "%.2f", orderModel.discountedAmount)
             }

             bind.plasticBagContainer.visibility = View.GONE
             bind.containerBagContainer.visibility = View.GONE
             bind.adjustmentContainer.visibility = View.GONE
             if(orderModel.vat_amount == 0.0) {
                 bind.vatContainer.visibility = View.GONE
             }else{
                 bind.vatContainer.visibility = View.VISIBLE
                 bind.vatAmount.text = "£ " + String.format( "%.2f", orderModel.vat_amount)
             }
             bind.total.text =
                 "£ " +String.format( "%.2f",(orderModel.payableAmount!!))
             var dlAddress = "Service charge is not included\n\n"
             if(businessdatadata.serviceCharge) {
                 dlAddress = "Service charge is not included\n\n"
             }else{
                 dlAddress = "\n\n"
             }
             if (orderModel.requesterGuest != null){
                 val customerModel: OrderData.RequesterGuest? = orderModel.requesterGuest
                 dlAddress += "Name : ${customerModel?.firstName} ${customerModel?.lastName}\n"
                 dlAddress += "Phone : ${customerModel?.phone}"
                 if (orderModel.shippingAddress != null) {
                     val address: OrderData.ShippingAddress? = orderModel.shippingAddress
                     if (address?.property != null) {
                         val pro: OrderData.ShippingAddress.Property = address.property
                         // CustomerAddressProperties pro = customerModel.addresses.get(0).properties;
                         val building = pro.house ?: ""
//                    val streetNumber = if (pro.street_number != null) pro.street_number else ""
                         val streetName = pro.state ?: ""
                         val city = pro.town ?: ""
                         val state = pro.state ?: ""
                         val zip = pro.postcode ?: ""
                         dlAddress += "\nAddress : $building $streetName\n$city $state $zip"
                     }
                 }
             }else{
                 if(orderModel.requester != null) {
                     val customerModel: OrderData.Requester? = orderModel.requester!!
                     dlAddress += "Name : ${customerModel?.name}\n"
                     dlAddress += "Phone : ${customerModel?.phone}"
                     if (orderModel.shippingAddress != null) {
                         val address: OrderData.ShippingAddress? = orderModel.shippingAddress
                         if (address?.property != null) {
                             val pro: OrderData.ShippingAddress.Property = address.property
                             // CustomerAddressProperties pro = customerModel.addresses.get(0).properties;
                             val building = pro.house ?: ""
//                    val streetNumber = if (pro.street_number != null) pro.street_number else ""
                             val streetName = pro.state ?: ""
                             val city = pro.town ?: ""
                             val state = pro.state ?: ""
                             val zip = pro.postcode ?: ""
                             dlAddress += "\nAddress : $building $streetName\n$city $state $zip"
                         }
                     }
                 }

             }

             var comment = "Comments : ${if(orderModel.comment != null) orderModel.comment else ""}"

             comment += """




        """.trimIndent()


             bind.comments.text = comment
             bind.comments.setTextSize(TypedValue.COMPLEX_UNIT_SP, header4.toFloat())
             bind.address.text = dlAddress
             bind.address.setTextSize(TypedValue.COMPLEX_UNIT_SP, header4.toFloat())


             if(!businessdatadata.vatNumber.isNullOrEmpty() || !businessdatadata.vatCompanyName.isNullOrEmpty()) {
                 bind.vatNumberCompany.text = "VAT no ${businessdatadata.vatNumber}"+", ${businessdatadata.vatCompanyName}"

                 bind.vatNumberCompany.visibility = View.VISIBLE
                 bind.vatNumberCompany.setTextSize(TypedValue.COMPLEX_UNIT_SP, footervatFontSize.toFloat())
             }else{
                 bind.vatNumberCompany.visibility = View.GONE
             }

             if(!businessdatadata.vatNote.isNullOrEmpty() ) {
                 bind.vatNote.text = "${businessdatadata.vatNote}"
                 bind.vatNote.visibility = View.VISIBLE
                 bind.vatNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, footervatFontSize.toFloat())
             }else{
                 bind.vatNote.visibility = View.GONE
             }

             val bitmaplist: Bitmap =  getBitmapFromView(bind.root)

             return  bitmaplist;
         }

         @SuppressLint("DefaultLocale")
         fun ButcherOrderPrint() : Bitmap {
             if(businessdatadata.selectPrinter!!.lowercase() == "label_printer" && orderModel.orderProducts != null && orderModel.orderProducts!!.isNotEmpty()) {
                 val bind: StickerprinterBinding = StickerprinterBinding.inflate(LayoutInflater.from(context))

//                 val dpi = businessdatadata.dpi ?: 203
//                 val widthMm = businessdatadata.label_width ?: 76
//                 val heightMm = businessdatadata.label_hight ?: 76
//                 // Convert mm to pixels
//                 val widthPx = (widthMm * dpi / 25.4f).toInt()
//                 val heightPx = (heightMm * dpi / 25.4f).toInt()
//                 val layoutParamsd = ViewGroup.LayoutParams(
//                     ViewGroup.LayoutParams.WRAP_CONTENT,
//                     ViewGroup.LayoutParams.WRAP_CONTENT
//                 )
//                 layoutParamsd.width = widthPx
//                 layoutParamsd.height = heightPx
//                 bind.root.layoutParams = layoutParamsd


                 val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                 val formatter1 = SimpleDateFormat("yyyy.MM.dd")
                 val formatter2 = SimpleDateFormat("HH:mm a")
                 var unitName = orderModel.orderProducts?.first()?.product?.property?.unit_of_sale
                 var item = orderModel.orderProducts?.first();
                 var  component: List<OrderData.OrderProduct.Component?>?
                 var  extraIteam: List<OrderData.OrderProduct.Component?>? = ArrayList()
                 if(orderModel.orderChannel?.uppercase() == "ONLINE") {
                     component = item?.components;
                 } else {
                     component = item?.components?.filter {i-> componentFilter(i)}
                     extraIteam = item?.components?.filter { i-> i?.product?.type == "EXTRA-COMPONENT"}
                 }


                 bind.itemName.text = orderModel.orderProducts?.first()?.product?.shortName
                 bind.itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.labelFontSize?.toFloat() ?: 22f)


                 bind.priceValue.text = "Price Per ${unitGet(item)} - £${orderModel.orderProducts?.first()?.netAmount.toString()}"
                 bind.priceValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.labelFontSize?.toFloat() ?: 22f)


                 bind.netwtvalue.text = "Weight - ${orderModel.orderProducts?.first()?.product?.property?.unit_amount} ${unitGet(item)}"
                 bind.netwtvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.labelFontSize?.toFloat() ?: 22f)

                 bind.businessName.text = businessname
                 bind.businessName.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.labelFontSize?.toFloat() ?: 22f)

                 var price = 0.0
                 var tareWeight : Double = if(item?.product?.property?.tare_weight?.isEmpty() == true) {
                     0.0;
                 }else{
                     item?.product?.property?.tare_weight?.toDouble() ?: 0.0
                 }
                 var unitAmount = if(item?.product?.property?.unit_amount?.isEmpty() == true) {
                     0.0
                 } else{
                     item?.product?.property?.unit_amount?.toDouble() ?: 0.0
                 }

                 price = if(item?.product?.property?.unit_product_type?.uppercase() == "WEIGHT") {
                     if(businessdatadata.weightMultiplyingPrice) {
                         (item.netAmount ?: 0.0) * ((if (unitAmount == 0.0) 1.0 else unitAmount) - tareWeight)
                     }else{
                         (item.netAmount ?: 0.0)
                     }

                 }else{
                     (item?.netAmount ?: 0.0)
                 }

                 var discount = item?.discountableAmount ?: 0.0;

                 if (!component.isNullOrEmpty()) {
                     for (section in component) {
                         if ((section?.components != null) && section.components.isNotEmpty()) {
                             for (section2 in section.components) {
                                 if (section2?.product?.shortName?.uppercase() != "NONE") {
                                     price += ((section2?.netAmount ?: 0.0) * (section2?.unit ?: 1));
                                 }
                             }

                         }

                         price += section?.netAmount ?: 0.0;
                     }
                 }

                 if (extraIteam != null) {
                     if (extraIteam.isNotEmpty()) {
                         for (extraItem in extraIteam) {
                             price += extraItem?.netAmount!!;
                         }
                     }
                 }


                 if(orderModel.orderChannel?.uppercase() != "ONLINE"){
                     if (item?.offer?.offer?.type == "X_FOR_Y" && item?.offer?.offer?.status == 1) {
                         var p = String.format("%.2f", getOrderOfferPrice(item))
                         price *=  p.toDouble()
                     }else if (item?.offer?.offer?.type == "X_FOR_£" && item?.offer?.offer?.status == 1) {
                         var p = String.format("%.2f", xForPoundOfferLocalDetailOrder(item, orderModel.orderProducts))
                         Log.e("price get", "getView: ${p}----")
                         price =  p.toDouble()
                     }else{
                         price *= (item?.unit ?: 1)
                     }

                     var totaldiscount = (price * (discount / 100))
                     price -= totaldiscount;
                 }

                 bind.totalvalue.text = "£${String.format("%.2f", price)}"
                 bind.totalvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.labelFontSize?.toFloat() ?: 22f)


                 if(item?.product?.property?.expire_date != null ) {
                     bind.expire.text = "Exp: ${item?.product?.property?.expire_date}"
                     bind.expire.setTextSize(TypedValue.COMPLEX_UNIT_SP, businessdatadata.labelFontSize?.toFloat() ?: 22f)

                 }else{
                     bind.expirecontainer.visibility = View.GONE
                 }


                  barcode = "${orderModel.orderProducts?.first()?.id}-${orderModel.orderProducts?.first()?.netAmount}-${orderModel.orderProducts?.first()?.product?.property?.unit_amount ?: 0}-${price}";

                 var barcodeBitmap = genBarcode(barcode)
                 val imageView = ImageView(context).apply {
                     setImageBitmap(barcodeBitmap)

                     // Set layout parameters if needed (e.g., dynamic width and height)
                     layoutParams = ViewGroup.LayoutParams(
                         ViewGroup.LayoutParams.WRAP_CONTENT,
                         ViewGroup.LayoutParams.WRAP_CONTENT
                     )
                 }
//                var layoutParams = ViewGroup.LayoutParams(
//                         businessdatadata.barcode_width?: 400,
//                    businessdatadata.barcode_hight?: 100,
//                     )
                 bind.items.removeAllViews()
                 bind.items.addView(imageView)
//                 bind.barcode.setImageBitmap(barcodeBitmap)

//                 var p = "Price/${unitGet(item)} ${orderModel.orderProducts?.first()?.netAmount.toString()}";
//                 var net = "Net: ${orderModel.orderProducts?.first()?.product?.property?.unit_amount ?: 0} ${unitGet(item)}"
//                 var t = "Total: ${String.format("%.2f", price)}"
//                 var ex= if(item?.product?.property?.expire_date == null) "" else "Exp: ${item?.product?.property?.expire_date}";
//
                 val bitmaplist: Bitmap =  getBitmapFromView(bind.root)
                 return  bitmaplist


             } else{
                 val printSize: Int = fontsize
                 val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                 val formatter = SimpleDateFormat("dd-MMM hh:mm a")
                 val bind: ButcherOrderPrintBinding = ButcherOrderPrintBinding.inflate(LayoutInflater.from(context))
                 bind.orderNo.text = "${orderModel.localId}"
                 bind.totalValue.text = "£ " +String.format( "%.2f",(orderModel.payableAmount!!))
                 bind.Date.text = "Date : ${parser.parse(orderModel.orderDate)
                     ?.let { formatter.format(it) }}"
                 bind.businessName.text = businessdatadata.businessname
                 var allitemsheight = 0
                 bind.items.removeAllViews()
                 var itemproduict = orderModel.orderProducts?.filter { i-> i?.product?.type == "ITEM" || i?.product?.type == "DYNAMIC" }
                 var sortIteam = itemproduict?.sortedWith(compareBy {it?.product?.property?.printorder?.toInt() ?: 0 })
                 if(!sortIteam.isNullOrEmpty()){
                     for (j in sortIteam.indices) {
                         val childView = getView(sortIteam, sortIteam[j],sortIteam.size, j, context, 0, printSize)
                         bind.items.addView(childView)
                         allitemsheight += childView!!.measuredHeight
                     }
                 }
                 var dlAddress = ""

                 if (orderModel.requesterGuest != null){
                     val customerModel: OrderData.RequesterGuest? = orderModel.requesterGuest
                     dlAddress += "Name : ${customerModel?.firstName} ${customerModel?.lastName}\n"
                     dlAddress += "Phone : ${customerModel?.phone}"
                     if (orderModel.shippingAddress != null) {
                         val address: OrderData.ShippingAddress? = orderModel.shippingAddress
                         if (address?.property != null) {
                             val pro: OrderData.ShippingAddress.Property = address.property
                             // CustomerAddressProperties pro = customerModel.addresses.get(0).properties;
                             val building = pro.house ?: ""
//                    val streetNumber = if (pro.street_number != null) pro.street_number else ""
                             val streetName = pro.state ?: ""
                             val city = pro.town ?: ""
                             val state = pro.state ?: ""
                             val zip = pro.postcode ?: ""
                             dlAddress += "\nAddress : $building $streetName\n$city $state $zip"
                         }
                     }
                 }else{
                     if(orderModel.requester != null) {
                         val customerModel: OrderData.Requester? = orderModel.requester!!
                         dlAddress += "Name : ${customerModel?.name}\n"
                         dlAddress += "Phone : ${customerModel?.phone}"
                         if (orderModel.shippingAddress != null) {
                             val address: OrderData.ShippingAddress? = orderModel.shippingAddress
                             if (address?.property != null) {
                                 val pro: OrderData.ShippingAddress.Property = address.property
                                 // CustomerAddressProperties pro = customerModel.addresses.get(0).properties;
                                 val building = pro.house ?: ""
//                    val streetNumber = if (pro.street_number != null) pro.street_number else ""
                                 val streetName = pro.state ?: ""
                                 val city = pro.town ?: ""
                                 val state = pro.state ?: ""
                                 val zip = pro.postcode ?: ""
                                 dlAddress += "\nAddress : $building $streetName\n$city $state $zip"
                             }
                         }
                     }

                 }

                 bind.address.text = dlAddress
                 bind.address.setTextSize(TypedValue.COMPLEX_UNIT_SP, header4.toFloat())

                 if(orderModel.barcode != null) {
                     var barcodeBitmap = genBarcode(orderModel.barcode!!)
                     bind.barcode.setImageBitmap(barcodeBitmap)
                 }
                 val bitmaplist: Bitmap =  getBitmapFromView(bind.root)
                 return  bitmaplist
             }
         }


         private fun View.setDimensionsInMillimeters() {
             val dpi = businessdatadata.dpi ?: 203
             val widthMm = businessdatadata.label_width ?: 76
             val heightMm = businessdatadata.label_hight ?: 76
             // Convert mm to pixels
             val widthPx = (widthMm * dpi / 25.4f).toInt()
             val heightPx = (heightMm * dpi / 25.4f).toInt()

             val layoutParams = this.layoutParams
             layoutParams.width = widthPx
             layoutParams.height = heightPx
             this.layoutParams = layoutParams
         }
         override fun doInBackground(vararg params: String?): Bitmap {
             if(businessdatadata.orderChannel?.uppercase() == "BUTCHER") {
                 val ButcherorderBitmap = ButcherOrderPrint()
                 return ButcherorderBitmap;
             }else{
                 val orderBitmap = eposWaiterorderPrint()
                 return orderBitmap
             }
         }


         override fun onPostExecute(result: Bitmap?) {
             super.onPostExecute(result)
             printBitmap(result)
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

    }