toast("检测是否开启无障碍模式")
auto.waitFor()
launch("com.android.camera");
sleep(3000);
var cameraSwitch = desc("前后置切换").findOne(500);
if (cameraSwitch) {
    cameraSwitch.click();
    sleep(3000);
}
toast("退出脚本")