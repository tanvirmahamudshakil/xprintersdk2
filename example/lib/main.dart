import 'package:flutter/material.dart';

import 'package:xprintersdk/Model/printerbusinessmodel.dart';
import 'package:xprintersdk/xprintersdk.dart';

import 'orderjson.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _xprintersdkPlugin = Xprintersdk();

  List<String> usbList = [];
  String selectUsb = "";

  labelPrinterInit() {
    _xprintersdkPlugin.labelPrinterInit();
  }

  getUsbList() async {
    usbList = await _xprintersdkPlugin.getLabelPrinterUsbList();
    setState(() {});
  }

  @override
  void initState() {
    getUsbList();
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text("Select Usb: ${selectUsb}"),
            ),
            SizedBox(height: 50),
            MaterialButton(
              onPressed: () {
                labelPrinterInit();
              },
              child: Text("Label Printer Init"),
            ),
            MaterialButton(
              onPressed: () {
                getUsbList();
              },
              child: Text("Get All Usb List"),
            ),

            DropdownButtonFormField(
              items: List.generate(usbList.length, (index) {
                var d = usbList[index];
                return DropdownMenuItem(value: d, child: Text(d));
              }),
              onChanged: (value) {
                selectUsb = value!;
                setState(() {});
              },
            ),
            MaterialButton(
              onPressed: () {
                _xprintersdkPlugin.labelPrinterUSbConnect(path_name: selectUsb);
              },
              child: Text("Connect Label Printer"),
            ),

            MaterialButton(
              onPressed: () {
                // _xprintersdkPlugin.labelPrinterPrintBarCode();
              },
              child: Text("Label Printer Bar Code"),
            ),

            MaterialButton(
              onPressed: () {
                var d = _xprintersdkPlugin.labelPrinterConnect();
                print("flutter lavel Printer ${d}");
              },
              child: Text("Label Printer Connect Check"),
            ),
            // MaterialButton(
            //   onPressed: () async {
            //     _xprintersdkPlugin.nyxPrinterInit();
            //   },
            //   child: const Text("NyxPrinter init"),
            // ),

            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.checkNyxPrinter();
            //     print(data.toString());
            //   },
            //   child: const Text("NyxPrinter Check"),
            // ),

            // MaterialButton(
            //   onPressed: () async {
            //     _xprintersdkPlugin.nyxPrinterPrintBitmap(printermodel, order2);
            //   },
            //   child: const Text("NyxPrinter print order"),
            // ),

            // MaterialButton(
            //     onPressed: () {
            //       _xprintersdkPlugin.XprinterInitialization();
            //     },
            //     child: const Text("Xprinter Initialization")),
            // MaterialButton(
            //     onPressed: () async {
            //       var data = await _xprintersdkPlugin.XPrinterConnectionCheck();
            //       print(data);
            //     },
            //     child: const Text("Xprinter connection check")),
            // MaterialButton(
            //   onPressed: () async {
            //     var data =
            //         await _xprintersdkPlugin.XPrinterConnect(printermodel);
            //     print(data);
            //   },
            //   child: const Text("Xprinter connect"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.XPrinterPrintOnLineData(
            //         printermodel, orderjson5);
            //     print(data);
            //   },
            //   child: const Text("Xprinter print"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.XPrinterPrintLocalData(
            //         printermodel, localorder);
            //   },
            //   child: const Text("local print"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.sunmiPrinterServiceInitialization();
            //   },
            //   child: const Text("Sunmi Printer Service Init"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.sunmiPrinterInitialization();
            //   },
            //   child: const Text("Sunmi Printer Init"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.sunmiPrintBitmap(
            //         printermodel, orderjson4);
            //     print("${data}");
            //     Future.delayed(const Duration(seconds: 2), () {
            //       showDialog(
            //           context: context,
            //           builder: (_) {
            //             return AlertDialog(
            //               title: Text("sunmi print $data"),
            //             );
            //           });
            //     });
            //   },
            //   child: const Text("Sunmi Print Bitmap"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.bitmapSave(printermodel, order2);
            //   },
            //   child: const Text("Bitmap Save"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.checkSunmiPrinter();
            //     print(data.toString());
            //   },
            //   child: const Text("Sunmi Printer Check"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.bookingRequestPrint(
            //         printermodel, bookingRequest);
            //     print(data.toString());
            //   },
            //   child: const Text("Bookign Request print"),
            // ),
            // MaterialButton(
            //   onPressed: () async {
            //     var data = await _xprintersdkPlugin.dailyReportPrint(
            //         printermodel, dailyReport);
            //     print(data.toString());
            //   },
            //   child: const Text("Daily Report print"),
            // )
          ],
        ),
      ),
    );
  }

  PrinterBusinessModel printermodel = PrinterBusinessModel(
      orderChannel: "EPOS",
      weightMultiplyingPrice: false,
      highlighttextsize: 20,
      dynamicCollection: "Collection",
      dynamicDelivery: "Delivery",
      dynamicEatIn: "Eat In",
      dynamicTakeaway: "Customer Waiting",
      autoPrint: true,
      highlight: 30,
      fontSize: 22,
      printOnCollection: 1,
      papersSize: 80,
      printOnDelivery: 1,
      printOnTableOrder: 1,
      printOnTackwayOrder: 1,
      printerConnection: "USBConnection",
      selectPrinter: "nyxprinter",
      showOrderNoInvoice: true,
      bluetoothAddress: "DC:0D:30:EE:30:2A",
      bluetoothName: "dsvsdvsd",
      businessname: "sdvsdvsdv",
      businessphone: "01932336565",
      ip: "192.168.0.104",
      businessaddress: "sdkjbvjsdhbvjhsbdv");
}
