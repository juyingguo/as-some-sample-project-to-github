/**
 * 1.滑动
 * 
 */

toast("检测是否开启无障碍模式")
auto.waitFor()
launch("com.ss.android.ugc.aweme.lite");
//var stay_time = 15*1000;
sleep(15000);
    execTask();
toast("退出脚本");
back();
back();
/**
 * 滑动
 */
function execTask(){
    console.log("execTask enter.");
    while(true){
        sleep(2000);

        gesture(1000, [350, 800], [350, 150]);

        sleep(15000);
    }
};