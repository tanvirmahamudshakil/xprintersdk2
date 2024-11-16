import 'package:flutter_test/flutter_test.dart';
import 'package:xprintersdk/xprintersdk.dart';
import 'package:xprintersdk/xprintersdk_platform_interface.dart';
import 'package:xprintersdk/xprintersdk_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockXprintersdkPlatform
    with MockPlatformInterfaceMixin
    implements XprintersdkPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final XprintersdkPlatform initialPlatform = XprintersdkPlatform.instance;

  test('$MethodChannelXprintersdk is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelXprintersdk>());
  });

  test('getPlatformVersion', () async {
    Xprintersdk xprintersdkPlugin = Xprintersdk();
    MockXprintersdkPlatform fakePlatform = MockXprintersdkPlatform();
    XprintersdkPlatform.instance = fakePlatform;

    expect(await xprintersdkPlugin.getPlatformVersion(), '42');
  });
}
