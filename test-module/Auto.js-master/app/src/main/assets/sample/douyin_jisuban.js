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
     //今日签到(前提是进入到赚钱任务页面)
     signInToday();

     //开宝箱赚金币(前提是进入到赚钱任务页面)
     makeMoneyByBox();
     
     // 走路赚金币。不可见的时候也能检测出来，可以直接点击打开对应界面，操作完成后，退出该界面
     execZouLuZHJBTask();
 
     //吃饭补贴-领金币
     execChiFanBuTieTask();
 
     sleep(1000);
     //退到主界面
     back();
     //翻页看视频
     execAutoPageTask();
 
 }
 
/**
 * 今日签到(前提是进入到赚钱任务页面)
 */
function signInToday(){

    sleep(5000);
    toastLog("signInToday");

    let temp=text("今日签到").find();
    // toastLog("signInToday--num:"+temp.length);
    if(temp.length==1){

        temp[0].click();

        //今日签到界面是自动弹出来的，所以直接点击签到领取
        let views=className("android.view.View").depth(16).selected(false).clickable(true).find();
        toastLog("signInToday-1-views.length:"+views.length);
        
        if(views.length==4){

            views[2].click();
        }

        sleep(1000);

        //点击其他空白区域关闭今日签到界面
        clickScreenActionByXY(device.width*0.2,device.height*0.1);

    }
    sleep(500);
}

/**
 * 开宝箱赚金币(前提是进入到赚钱任务页面)
 */
function makeMoneyByBox(){
    sleep(2000);
    toastLog("makeMoneyByBox");

    //点击宝箱图案
    let views=className("android.view.View").depth(13).selected(false).clickable(true).find();
    toastLog("makeMoneyByBox-1-views.length:"+views.length);
    
    if(views.length==1){
        views[0].click();
    }

    sleep(1000);

    //关闭宝箱界面
    let views2=className("android.view.View").depth(16).selected(false).clickable(true).find();
    toastLog("makeMoneyByBox-2-views2.length:"+views2.length);
    
    if(views2.length==4){
        views2[3].click();    //2表示点击了再看广告领取金币; 3是关闭
    }

    if(views2.length==3){
        views2[2].click();    
    }
}
 /**
  * 1.向上滑动翻页看视频。
  * 2.看视频中会有提示金币框，关闭弹框
  */
 function execAutoPageTask(){
     console.log("execAutoPageTask enter.");
     while(true){
         sleep(2000);
 
         //看视频中会有提示金币框，关闭弹框，如果失败会，会引发下面其余代码无法执行   
         //通多id获取控件，通过depth，两者都添加，防止有时候，无法执行。     
         try{
             var dialogTipClose = id("bai").findOne(2000);
             if(dialogTipClose){
                 dialogTipClose.click();
             }            
         }catch(error){
             // console.log("execAutoPageTask use id,error:" + error);
         }
         try{
             className("android.widget.ImageView").depth(4).clickable(true).findOne(2000).click();            
             console.log("execAutoPageTask close dialog(use depth),click ok.");
         }catch(error){
             // console.log("execAutoPageTask depth,error:" + error);
         }
 
         gesture(1000, [350, 800], [350, 150]);
 
         sleep(8000);
     }
 };
 
 /**
  * 走路赚金币。不可见的时候也能检测出来，可以直接点击打开对应界面，操作完成后，退出该界面
  */
 function execZouLuZHJBTask(){
     console.log("execZouLuZHJBTask enter.");
     sleep(4000);
     try{
         var viewZouLuZHJB = text("走路赚金币").findOne(1000);
         if(viewZouLuZHJB){
             console.log("viewZouLuZHJB exist,");
 
             // var viewZouLuZHJB_bounds = viewZouLuZHJB.bounds();
             // console.log("viewZouLuZHJB exist,left:" + viewZouLuZHJB_bounds.left + " top" + viewZouLuZHJB_bounds.top);
 
             // console.log("viewZouLuZHJB(走路赚金币) exist,click bounds.");
             // click(viewZouLuZHJB_bounds.left + 10,viewZouLuZHJB_bounds.top+10);
 
             viewZouLuZHJB.click();
             sleep(2000);
 
             ///走路-领金币
             try {
                 className("android.view.View").clickable(true).depth(13).findOne(2000).click();
             } catch (error) {
                 console.log("viewZouLuZHJB 走路-领金币 error:" + error);
             }
         
             //返回上一个界面
             sleep(1000);
             back();
         }else{
             console.log("viewZouLuZHJB not exist.");
         }
     }catch(error){
         console.log("viewZouLuZHJB(走路赚金币) error:" + error);
     }
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
             sleep(3000);
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