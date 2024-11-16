
import 'dart:convert';

import 'package:flutter/services.dart';

import 'Model/printerbusinessmodel.dart';

class Xprintersdk {

  final _methodChannel = const MethodChannel('xprintersdk');
final String _xPrinterIntitalization = "xPrinterIntitalization";
  final String _xPrinterConnectionCheck = "xPrinterConnectionCheck";
  final String _xPrinterConnect = "xPrinterConnect";
  final String _xPrinterPrintOnlineData = "xPrinterPrintOnlineData";
  final String _xPrinterPrintLocalData = "xPrinterPrintLocalData";
  final String _sunmiPrinterService = "sunmiPrinterService";
  final String _sunmiPrinterInit = "sunmiPrinterInit";
  final String _sunmiPrint = "sunmiPrintBitmap";
  final String _bitmapImageSave = "bitmapImageSave";
  final String _sunmiPrinterCheck = "sunmiPrinterCheck";
  final String _xPrinterbookingRequest = "XprinterbookingRequest";
  final String _dailyreportPrint = "dailyreportPrint";
  final String _nyxPrinterPrint = "nyxPrinterPrint";
  final String _nyxPrinterInit = "nyxPrinterInit";
  final String _nyxPrinterCheck = "nyxPrinterCheck";
  final String _dailyreportImagePrint = "dailyreportImagePrint";
  final String _propertyReturnPrint = "propertyReturnPrint";

  final String _labelPrinterInit = "labelPrinterInit";
  final String _labelPrinterUsbList = "labelPrinterusbList";
  final String _labelPrinterConnectUSB = "labelPrinterConnectUSB";
  final String _labelPrinterPrintBarCode = "labelPrinterPrintBarCode";
  final String _labelPrinterConnection = "labelPrinterConnection";
  final String _printer80PrintImage = "printer80PrintImage";

  Future<String?> getPlatformVersion() async {
    final version = await _methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  Future XprinterInitialization() async {
    await _methodChannel.invokeMethod(_xPrinterIntitalization);
  }

  Future sunmiPrinterServiceInitialization() async {
    await _methodChannel.invokeMethod(_sunmiPrinterService);
  }

  Future sunmiPrinterInitialization() async {
    await _methodChannel.invokeMethod(_sunmiPrinterInit);
  }

  Future<bool> XPrinterConnectionCheck() async {
    return await _methodChannel.invokeMethod(_xPrinterConnectionCheck);
  }

  Future<bool> XPrinterConnect(PrinterBusinessModel printermodel) async {
    Map<String, dynamic> quary = {"printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_xPrinterConnect, quary);
  }

  Future XPrinterPrintOnLineData(
    PrinterBusinessModel printermodel,
    Map<String, Object?> orderiteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": orderiteam, "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_xPrinterPrintOnlineData, quary);
  }

  Future XPrinterPrintLocalData(
    PrinterBusinessModel printermodel,
    Map<String, Object?> orderiteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": jsonDecode(jsonEncode(orderiteam)), "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_xPrinterPrintLocalData, quary);
  }

  Future sunmiPrintBitmap(
    PrinterBusinessModel printermodel,
    Map<String, Object?> orderiteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": orderiteam, "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_sunmiPrint, quary);
  }

  Future nyxPrinterInit() async {
    return await _methodChannel.invokeMethod(_nyxPrinterInit);
  }

  Future checkNyxPrinter() async {
    return await _methodChannel.invokeMethod(_nyxPrinterCheck);
  }

  Future nyxPrinterPrintBitmap(
    PrinterBusinessModel printermodel,
    Map<String, Object?> orderiteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": orderiteam, "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_nyxPrinterPrint, quary);
  }

  Future bitmapSave(
    PrinterBusinessModel printermodel,
    Map<String, Object?> orderiteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": orderiteam, "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_bitmapImageSave, quary);
  }

  Future checkSunmiPrinter() async {
    return await _methodChannel.invokeMethod(_sunmiPrinterCheck);
  }

  Future bookingRequestPrint(
    PrinterBusinessModel printermodel,
    Map<String, Object?> bookingrequestIteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": jsonDecode(jsonEncode(bookingrequestIteam)), "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_xPrinterbookingRequest, quary);
  }

  Future<bool> dailyReportPrint(
    PrinterBusinessModel printermodel,
    Map<String, Object?> bookingrequestIteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": jsonDecode(jsonEncode(bookingrequestIteam)), "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_dailyreportPrint, quary);
  }

  Future<bool> dailyReportImagePrint(
    PrinterBusinessModel printermodel,
    Map<String, Object?> bookingrequestIteam,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": jsonDecode(jsonEncode(bookingrequestIteam)), "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_dailyreportImagePrint, quary);
  }

  Future<bool> propertyShopReturnPrint(
    PrinterBusinessModel printermodel,
    Map<String, Object?> propertyreturnData,
  ) async {
    Map<String, dynamic> quary = {"orderiteam": jsonDecode(jsonEncode(propertyreturnData)), "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_propertyReturnPrint, quary);
  }

  // label printer

  Future<bool> labelPrinterInit() async {
    return await _methodChannel.invokeMethod(_labelPrinterInit);
  }

  Future<List<String>> getLabelPrinterUsbList() async {
    var list = await _methodChannel.invokeMethod<List>(_labelPrinterUsbList);
    return list?.map((e) => e.toString()).toList() ?? [];
  }

  Future<bool> labelPrinterUSbConnect({required String path_name}) async {
    Map<String, dynamic> quary = {"path_name": path_name};
    return await _methodChannel.invokeMethod(_labelPrinterConnectUSB, quary);
  }

  Future<bool> labelPrinterPrintBarCode({
    required PrinterBusinessModel printermodel,
    required Map<String, Object?> orderiteam,
  }) async {
    Map<String, dynamic> quary = {"orderiteam": jsonDecode(jsonEncode(orderiteam)), "printer_model_data": jsonEncode(printermodel.toJson())};

    return await _methodChannel.invokeMethod(_labelPrinterPrintBarCode, quary);
  }

  Future<bool> labelPrinterConnect() async {
    return await _methodChannel.invokeMethod(_labelPrinterConnection);
  }

  Future printer80PrintImage({
    required PrinterBusinessModel printermodel,
    required Map<String, Object?> orderiteam,
  }) async {
    Map<String, dynamic> quary = {"orderiteam": jsonDecode(jsonEncode(orderiteam)), "printer_model_data": jsonEncode(printermodel.toJson())};
    return await _methodChannel.invokeMethod(_printer80PrintImage, quary);
  }
}
