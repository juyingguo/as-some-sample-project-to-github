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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary( "native-lib" );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            toCheckPermission();
        }
        setContentView( R.layout.activity_main );

        // Example of a call to a native method
        TextView tv = (TextView) findViewById( R.id.sample_text );
        tv.setText( stringFromJNI() );
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
