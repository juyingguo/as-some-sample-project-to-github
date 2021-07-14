package com.demo.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String TAG=getClass().getName();
    Button runBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runBtn= (Button) findViewById(R.id.runBtn);
    }

    public void clickToOpenContacts(View view) {
        Log.i(TAG, "clickToOpenContacts.");
    }
    /**
     * 点击按钮对应的方法
     * @param v
     */
    public void runMyUiautomator(View v){
        Log.i(TAG, "runMyUiautomator: ");
        new UiautomatorThread().start();
    }

    /**
     * 运行uiautomator是个费时的操作，不应该放在主线程，因此另起一个线程运行
     */
    class UiautomatorThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.i(TAG, "UiautomatorThread,run.");

            //String command=generateCommand("com.demo.uiautomator2testcase", "OpenAppTest", "demo");
//            String command=generateCommandAndroidX("com.demo.myapplication", "OpenAppTest", "demo");
            String command="pm list package -3";
            RootCmd.execRootCmd(command);
            CMDUtils.CMD_Result rs= CMDUtils.runCMDWithRoot(command,true,true);
            Log.e(TAG, "run: " + rs.error + "-------" + rs.success);
        }

        /**
         * 生成命令
         * @param pkgName 包名
         * @param clsName 类名
         * @param mtdName 方法名
         * @return
         */
        public  String generateCommand(String pkgName, String clsName, String mtdName) {
            Log.i(TAG, "generateCommand.");
            /*String command = "am instrument  --user 0 -w -r   -e debug false -e class "
                    + pkgName + "." + clsName + "#" + mtdName + " "
                    + pkgName + ".test/android.support.test.runner.AndroidJUnitRunner";*/
            String command = "am instrument -w -r -e debug false -e class "
                    + pkgName + "." + clsName + "#" + mtdName + " "
                    + pkgName + ".test/android.support.test.runner.AndroidJUnitRunner";
            return command;
        }

        /**
         * 生成命令
         * @param pkgName 包名
         * @param clsName 类名
         * @param mtdName 方法名
         * @return
         */
        public  String generateCommandAndroidX(String pkgName, String clsName, String mtdName) {
            Log.i(TAG, "generateCommand.");
            /*String command = "am instrument --user 0 -w -r -e debug false -e class "
                    + pkgName + "." + clsName + "#" + mtdName + " "
                    + pkgName + ".test/androidx.test.runner.AndroidJUnitRunner";*/
            String command = "am instrument -w -r -e debug false -e class "
                    + pkgName + "." + clsName + "#" + mtdName + " "
                    + pkgName + ".test/androidx.test.runner.AndroidJUnitRunner";
            return command;
        }
    }

}
