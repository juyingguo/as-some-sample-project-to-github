/**
 * 1.前后置切换
 * 2.拍照一张
 * 3.切换到录像
 * 3.1 录像5s后停止。
 * 4.自动退出相机应用。
 * 
 */

toast("检测是否开启无障碍模式")
auto.waitFor()
launch("com.android.camera");
sleep(3000);
var cameraSwitch = desc("前后置切换").findOne(500);
if(cameraSwitch){
    console.log("cameraSwitch compontent get ok.");
    
    //从默认后置摄像头切换到前置摄像头。
    cameraSwitch.click();
    sleep(3000);
    //拍照
    var takePhoto = desc("拍摄").findOne(500);
    if(takePhoto){
        console.log("takePhoto compontent get ok.");
        takePhoto.click();
        toast("拍照成功!");
        sleep(2000);

        execCaptureTask();

        toast("退出脚本");
        back();
        back();
    }
    
}
/**
 * 录像
 */
function execCaptureTask(){
    console.log("execCaptureTask enter.");
    var cpatureTab = text("录像").findOne(500);
    if(cpatureTab){
        console.log("cpatureTab compontent get ok.");
        cpatureTab.click();

        sleep(3000);
        //拍摄-录像时
        var capture = desc("拍摄").findOne(500);
        if(capture){
            console.log("capture compontent get ok.");
            capture.click();

            sleep(5000);//录像
            console.log("capture 5s finish.");

            capture.click();//停止，因desc相同，可以复用。
            sleep(1000);
        } 

    }
};