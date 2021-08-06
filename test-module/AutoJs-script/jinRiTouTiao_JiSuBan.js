/**
 * 1.滑动
 * 
 */

toast("检测是否开启无障碍模式")
auto.waitFor()
launch("com.ss.android.article.lite");
//var stay_time = 15*1000;
sleep(5000);

    ///makeMoneyView();
    execHongBao();
    //execAutoPageTask();
// toast("退出脚本");
// back();
// back();
/**
 * 执行左上角，红包按钮
 */
 function execHongBao(){
    console.log("execHongBao enter.");
    // className("android.widget.TextView").text("热榜").findOne().click();
    var idHongBao = id("acv").findOne();
    if(idHongBao){
        console.log("execHongBao,idHongBao component find ok.");
        idHongBao.click();
    }

    // var depthLaiZhuanQian =  className("android.widget.RelativeLayout").depth(5).findOnce(1000);

    // if(depthLaiZhuanQian){
    //     console.log("execLaiZhuanQian component find ok.");
    //     depthLaiZhuanQian.click();
    // }
};
function makeMoneyView() {

    //通过属性来找到点赞控件
    toastLog("makeMoney");

    let frameLayouts=className("android.widget.FrameLayout").depth(3).selected(false).clickable(true).find();

    toastLog("videoLike--frameLayouts.length:"+frameLayouts.length);
    if(frameLayouts.length==1){  //注意如果是6个的时候，该如何
        frameLayouts[0].click();
    }

}

/**
 * 滑动
 */
function execAutoPageTask(){
    console.log("execAutoPageTask enter.");
    while(true){
        //sleep(2000);

        gesture(1000, [350, 800], [350, 150]);

        sleep(12000);
    }
};