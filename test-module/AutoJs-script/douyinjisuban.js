/**
 * 1.滑动
 * 
 */

toast("检测是否开启无障碍模式")
auto.waitFor()
launch("com.ss.android.ugc.aweme.lite");
//var stay_time = 15*1000;
sleep(5000);

    // makeMoneyView();
    ///execLaiZhuanQian();
    execAutoPageTask();
toast("退出脚本");
back();
back();
/**
 * 执行来赚钱
 */
 function execLaiZhuanQian(){
    console.log("execLaiZhuanQian enter.");
    // var idLaiZhuanQian = id("bx7").findOne(500);
    // if(idLaiZhuanQian){
    //     console.log("execLaiZhuanQian component find ok.");
    //     idLaiZhuanQian.click();
    // }

    var depthLaiZhuanQian =  className("android.widget.RelativeLayout").depth(5).findOnce(1000);

    if(depthLaiZhuanQian){
        console.log("execLaiZhuanQian component find ok.");
        depthLaiZhuanQian.click();
    }
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
        sleep(2000);

        //关闭弹框，如果失败会，会引发下面其余代码无法执行         
        try{
            var dialogTipClose = id("bai").findOne(2000);
            if(dialogTipClose){
                dialogTipClose.click();
            }            
        }catch(error){
            console.log("execAutoPageTask id,error:" + error);
        }
        try{
            className("android.widget.ImageView").depth(4).clickable(true).findOne(2000).click();            
        }catch(error){
            console.log("execAutoPageTask depth,error:" + error);
        }

        gesture(1000, [350, 800], [350, 150]);

        sleep(13000);
    }
};