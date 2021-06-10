package aplay.testopengles;

import android.Manifest;
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
        toCheckPermission();
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
