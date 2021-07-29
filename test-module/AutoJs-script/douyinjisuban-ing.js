/**
 * 1.滑动
 * 
 */

toast("检测是否开启无障碍模式")
auto.waitFor()
launch("com.ss.android.ugc.aweme.lite");
//var stay_time = 15*1000;
sleep(5000);

    // close();

    makeMoneyView();
    ///execLaiZhuanQian();
    ///execAutoPageTask();
// toast("退出脚本");
// back();
// back();
/**
 * 执行来赚钱
 */
function makeMoneyView() {

    //通过属性来找到点赞控件
    toastLog("makeMoney");

    // let frameLayouts=className("android.widget.FrameLayout").depth(3).clickable(true).findOne();
    let frameLayouts=className("android.widget.FrameLayout").depth(3).selected(false).clickable(true).find();

    toastLog("videoLike--frameLayouts.length:"+frameLayouts.length);
    if(frameLayouts.length==1){  //注意如果是6个的时候，该如何
        frameLayouts[0].click();
    }

    //走路赚钱-id=a90
    // var zouLuZhuanQian = id("a90").findOne(1000);
    // if(zouLuZhuanQian){
    //     zouLuZhuanQian.click();
    //     sleep(2000);

    //     //
    //     var zouLuZhuanQianClick=className("android.view.View").clickable(true).depth(13).selected(false).findOnce(1000);
    //     if(zouLuZhuanQianClick){ 
    //         zouLuZhuanQianClick.click();
    //         sleep(2000);
    //     }

    // }


    ///金币转换提示，clickable一直变
    ///var jinBiZhuanHuanTip = className("android.view.View").text(金币每天凌晨左右自动兑换成现金).clickable(true).depth(14).findOnce(1000);
    // sleep(5000);
    // var jinBiZhuanHuanTip = className("android.view.View").clickable(true).depth(14).findOnce();
    // if(jinBiZhuanHuanTip != null){
    //     jinBiZhuanHuanTip.click();

    //     sleep(5000);
    //     //关闭提示框
    //     className("android.view.Button").text("closebutton").findOnce(1000).click;
    // }

    ///5设置
    ///5.1 但是打开的是。外层id="bqy"，但是点击后为：拍视频开红包。
    // var set = id("bqy").findOne().click();
    //5.2 通过下面depth 是可以的。
    // className("android.view.View").clickable(true).depth(14).findOne().click();

    ///6 关闭。id=a8p。可以用
    // var close = id("a8p").findOne().click();
    //6.1 通过坐标点击。ok
    // click(32+5,64+5);


    ///7. 看赛事。目前失败
    // id("olympics_gold_bonus").findOne().click();

    ///8. 邀请好友
    ///8.1 大额福利，实际点击也无效。
    // text("大额福利").findOne().click();
    // var daily_invite_apprentice = id("daily_invite_apprentice").findOne();
    // if(daily_invite_apprentice != null){
    //     toastLog("daily_invite_apprentice find.");
    //     daily_invite_apprentice.click();
    // }    
    ///8.1 通过坐标点击。ok。需要确认点击的控件范围坐标。
    // click(30+5,844+5);

    ///9 判断控件是否存在，异常捕获。
    // try{
    //     var id = id("123-ni-hao-test").findOne();
    //     if(id){
    //         toastLog("123-ni-hao-test id exist.");
    //     }else{
    //         toastLog("123-ni-hao-test id not exist.");
    //     }
    // }catch(error){
    //     console.log("123-ni-hao-test id not exist,error:" + error);
    // }

    ///10 看广告赚金币。    
    // try{
    //     var viewKGGZHJB = text("看广告赚金币").findOne();
    //     if(viewKGGZHJB){
    //         console.log("viewKGGZHJB exist,");

    //         var viewKGGZHJB_bounds = viewKGGZHJB.bounds();
    //         console.log("viewKGGZHJB exist,left:" + viewKGGZHJB_bounds.left + " top" + viewKGGZHJB_bounds.top);

    //         click(viewKGGZHJB_bounds.left -3,viewKGGZHJB_bounds.top-3);
    //     }else{
    //         toastLog("viewKGGZHJB not exist.");
    //     }
    // }catch(error){
    //     console.log("viewKGGZHJB not exist,error:" + error);
    // }
    
    ////11 走路赚金币。不可见的时候也能检测出来，可以直接点击打开对应界面，操作完成后，退出该界面
    execZouLuZHJBTask();

    execChiFanBuTieTask();

    sleep(1000);
    back();
    execAutoPageTask();

    // back();
    // execAutoPageTask();
}

/**
 * 滑动翻页
 */
function execAutoPageTask(){
    console.log("execAutoPageTask enter.");
    while(true){
        sleep(2000);

        //关闭弹框，如果失败会，会引发下面其余代码无法执行         
        // try{
        //     var dialogTipClose = id("bai").findOne(2000);
        //     if(dialogTipClose){
        //         dialogTipClose.click();
        //     }            
        // }catch(error){
        //     console.log("execAutoPageTask id,error:" + error);
        // }
        try{
            className("android.widget.ImageView").depth(4).clickable(true).findOne(2000).click();            
            console.log("execAutoPageTask depth,click ok.");
        }catch(error){
            console.log("execAutoPageTask depth,error:" + error);
        }

        gesture(1000, [350, 800], [350, 150]);

        sleep(13000);
    }
};

/**
 * 走路赚金币。不可见的时候也能检测出来，可以直接点击打开对应界面，操作完成后，退出该界面
 */
function execZouLuZHJBTask(){
    console.log("execZouLuZHJBTask enter.");
    sleep(5000);
    var flag = true;
    while(flag){
        try{
            var viewZouLuZHJB = text("走路赚金币").findOne(3000);
            if(viewZouLuZHJB){
                console.log("viewZouLuZHJB exist,");
    
                // var viewZouLuZHJB_bounds = viewZouLuZHJB.bounds();
                // console.log("viewZouLuZHJB exist,left:" + viewZouLuZHJB_bounds.left + " top" + viewZouLuZHJB_bounds.top);
    
                // console.log("viewZouLuZHJB(走路赚金币) exist,click bounds.");
                // click(viewZouLuZHJB_bounds.left + 10,viewZouLuZHJB_bounds.top+10);

                viewZouLuZHJB.click();
                sleep(3000);

                ///走了金币-领金币
                try {
                    className("android.view.View").clickable(true).depth(13).findOne(2000).click();
                } catch (error) {
                    console.log("viewZouLuZHJB 走了金币-领金币 not exist,error" + error);
                }
                
                break;
            }else{
                console.log("viewZouLuZHJB not exist.");
            }
        }catch(error){
            console.log("viewZouLuZHJB(走路赚金币) not exist,继续滑动300px.");
            gesture(1000, [350, 800], [350, 500]);
        } 
        sleep(1000);
    }
    
    //返回上一个界面
    sleep(1000);
    back();
    console.log("execZouLuZHJBTask end.");
}
/**
 * 吃饭补贴-领金币。
 * 1.操作完成后，退出该界面
 */
 function execChiFanBuTieTask(){
    console.log("execChiFanBuTieTask enter.");
    sleep(1000);
    //未领过，可以点击，弹框，领过后，再关闭。
    //已经领过，不可点击
    try{

        var viewChiFanBuTie = text("吃饭补贴").findOne(1000);
        if(viewChiFanBuTie){
            viewChiFanBuTie.click();
            sleep(4000);
        }

        var viewChiFanBuTieLinQu = className("android.view.View").clickable(true).depth(13).findOne(2000);
        if(viewChiFanBuTieLinQu){
            console.log("execChiFanBuTieTask viewChiFanBuTieLinQu exist.");

            // var viewZouLuZHJB_bounds = viewZouLuZHJB.bounds();
            // console.log("viewZouLuZHJB exist,left:" + viewZouLuZHJB_bounds.left + " top" + viewZouLuZHJB_bounds.top);

            // console.log("viewZouLuZHJB(走路赚金币) exist,click bounds.");
            // click(viewZouLuZHJB_bounds.left + 10,viewZouLuZHJB_bounds.top+10);

            ///点击领金币
            viewChiFanBuTieLinQu.click();

            sleep(1500);
            //领金币成功后，关闭弹框
            try {
                var closeImage = className("android.widget.Image").depth(13).clickable(true).findOne(1000);
                if(closeImage){
                    closeImage.click();
                }
            } catch (error) {
                console.log("execChiFanBuTieTask viewChiFanBuTieLinQu 领金币成功后，关闭弹框,error:" + error);
            }

        }else{
            console.log("execChiFanBuTieTask viewChiFanBuTieLinQu component not find(clickable(false)) 已经领过，不可点击.");
            viewChiFanBuTie = className("android.view.View").clickable(false).depth(13).findOne(2000);
            // if(viewChiFanBuTie){
            //     console.log("execChiFanBuTieTask viewChiFanBuTieLinQu component not find 已经领过，不可点击.");
            // }
        }
    }catch(error){
        console.log("execChiFanBuTieTask viewChiFanBuTie(吃饭补贴) error:" + error);
    } 
    
    //返回上一个界面
    sleep(1000);
    back();
    console.log("execChiFanBuTieTask end.");
}

// function close(){
//     console.log("close() enter.");
//     sleep(6000);
//     //领金币成功后，关闭弹框
//     try {
//         var closeImage = className("android.widget.Image").depth(13).clickable(true).findOne(1000);
//         if(closeImage){
//             closeImage.click();
//         }
//     } catch (error) {
//         console.log("execChiFanBuTieTask viewChiFanBuTieLinQu 领金币成功后，关闭弹框,error:" + error);
//     }
// }