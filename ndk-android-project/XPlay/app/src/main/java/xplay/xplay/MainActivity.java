/*******************************************************************************
**                                                                            **
**                     Jiedi(China nanjing)Ltd.                               **
**	               创建：夏曹俊，此代码可用作为学习参考                       **
*******************************************************************************/

/*****************************FILE INFOMATION***********************************
**
** Project       : FFmpeg
** Description   : FFMPEG项目创建示例
** Contact       : xiacaojun@qq.com
**        博客   : http://blog.csdn.net/jiedichina
**		视频课程 : 网易云课堂	http://study.163.com/u/xiacaojun		
				   腾讯课堂		https://jiedi.ke.qq.com/				
				   csdn学院		http://edu.csdn.net/lecturer/lecturer_detail?lecturer_id=961	
**                 51cto学院	http://edu.51cto.com/lecturer/index/user_id-12016059.html	
** 				   下载最新的ffmpeg版本 ffmpeg.club
**                 
**   安卓流媒体播放器 课程群 ：23304930 加入群下载代码和交流
**   微信公众号  : jiedi2007
**		头条号	 : 夏曹俊
**
*******************************************************************************/
//！！！！！！！！！ 加群23304930下载代码和交流


package xplay.xplay;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "MainActivity";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary( "native-lib" );
    }

    private Button bt;
    private SeekBar seek;
    private Thread th;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        //去掉标题栏
        supportRequestWindowFeature( Window.FEATURE_NO_TITLE);
        //全屏，隐藏状态
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //屏幕为横屏
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );


        setContentView( R.layout.activity_main );
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            toCheckPermission();
        }
        bt = findViewById( R.id.open_button );
        seek = findViewById( R.id.aplayseek );
        seek.setMax(1000);
        bt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("XPlay","open button click!");
                //打开选择路径窗口
                Intent intent = new Intent();
                intent.setClass( MainActivity.this ,OpenUrl.class);
                startActivity( intent );


            }
        } );

        System.out.println("getExternalCacheDir():" + getExternalCacheDir());
        getOpenGLInfo();

        th = new Thread(this);
        th.start();
    }
    @Override
    public void run(){
        for (;;){
            seek.setProgress((int)( 1000*PlayPos()));
            SystemClock.sleep(40);
        }
    }
    public native double PlayPos();
    private void getOpenGLInfo(){
        ActivityManager am =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        String s = info.reqGlEsVersion+"";
        String ver = Integer.toHexString(Integer.parseInt(s));
//        tv.setText("reqGlEsVersion = " + ver);
        System.out.println("reqGlEsVersion = " + ver);
    }
    private static final int RC_PREM =100;
    private static final String[] perm ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private void toCheckPermission() {
        Log.d(TAG, "toCheckPermission.");
        if (EasyPermissions.hasPermissions(this,perm)){
            Log.d(TAG, "toCheckPermission,hasPermissions.");
            // 应用已经拥有了权限
        }else {
            // 应用没有拥有权限
            Log.d(TAG, "toCheckPermission,to call requestPermissions.");
            EasyPermissions.requestPermissions(
                    this,
                    "use cancel"/*getResources().getString(R.string.request_loacation)*/,//使用官方提供的弹窗(`AppSettingsDialog`)的时候会用到这个字符串,作为用户拒绝之后弹窗的内容
                    RC_PREM,
                    perm
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult,requestCode" + requestCode);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
