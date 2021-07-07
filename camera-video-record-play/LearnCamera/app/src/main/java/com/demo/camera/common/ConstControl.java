package com.demo.camera.common;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.json.JSONArray;

/**
 * Created by jack_zou on 2016/11/18.
 */

public class ConstControl {
    public  static final String TTS_EXT_REQUEST = "com.demo.ibotnvoice.TTS_EXT_REQUEST";
    public static final String EXPRESSION_START_VR = "START_VIDEO_REC";
    public static final String EXPRESSION_STOP_VR = "STOP_VIDEO_REC";
    public static final String EXPRESSION_ACTION_SYS_STATE = "expression.ExpressionAnimation.ACTION_SYS_STATE";
    public static final String EXPRESSION_STATE_TAG ="SYS_STATE";
    public static final String EXPRESSION_ACTION_FUNC_OP = "expression.ExpressionAnimation.ACTION_FUNC_OP";
    public static final String EXPRESSION_CMD_OP = "CMD_OP";
    public static final String EXPRESSION_CMD_ENABLE = "ENABLE";
    public static final String EXPRESSION_CMD_DISABLE = "DISABLE";
    public static final String EXPRESSION_START_TRACKING= "START_TRACKING";
    public static final String EXPRESSION_STOP_TRACKING= "STOP_TRACKING";
    /**
     * 设置笑脸动画状态接口
     * @param state
     */
    public static void setExpressionAnimationState(Context context, String state) {
        Intent intentC = new Intent(ConstControl.EXPRESSION_ACTION_SYS_STATE);
        intentC.putExtra(ConstControl.EXPRESSION_STATE_TAG, state);
        context.sendBroadcast(intentC);
    }
    /**
     *  笑脸动画开关接口
     *  @param context
     *  @param enable   开、关
     */
    public static void expressionAnimationEnableSet(Context context, boolean enable) {
//        Intent intentC = new Intent(EXPRESSION_ACTION_FUNC_OP);
//        if (enable == true) {
//            intentC.putExtra(EXPRESSION_CMD_OP, EXPRESSION_CMD_ENABLE);
//        }
//        else {
//            intentC.putExtra(EXPRESSION_CMD_OP, EXPRESSION_CMD_DISABLE);
//        }
//        context.sendBroadcast(intentC);
    }
    /**
     * 播放语音接口
     * @param content  语音内容
     */
    public static void startTtsSpeaker(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            Intent intent = new Intent(TTS_EXT_REQUEST);
            intent.putExtra("content", content);
            context.sendBroadcast(intent);
        }
    }
    /**
     * 向MessageServer 发送指令
     * @param send_to  暂时没有用到
     * @param name 消息名
     * @param type 消息类型
     * @param description  类型描述
     * @param date  日期
     * @param time  时间
     */
    public static void sendMessageToMessageServer(Context context, JSONArray send_to, String name, String type, String description, String date, String time){
        Intent intent = new Intent("com.demo.ibotncamera.SEND.MESSAGE.SERVICE");
        intent.putExtra("jsonarray","");
        intent.putExtra("message_name",name);
        intent.putExtra("message_type",type);
        intent.putExtra("message_description",description);
        intent.putExtra("message_date",date);
        intent.putExtra("message_time",time);
        context.sendBroadcast(intent);

    }
    /**
     * 向uartService发送改变运行模式指令
     * @param cmd   指令
     */
    public static void sendChangeModeToUartService(Context context, int cmd){
        Intent intent =new Intent("com.demo.ibotncamera.SEND.UART.CHANGEMODEL.CMD");
        intent.putExtra("cmd",cmd);
        context.sendBroadcast(intent);
    }
    /**
     * 向uartservice发送指令
     * @param cmd   指令
     */
    public static void sendCmdToUartService(Context context, int cmd){
        Intent intent =new Intent("com.demo.ibotncamera.SEND.UART.CMD.ONLY");
        intent.putExtra("cmd",cmd);
        context.sendBroadcast(intent);
    }
    /**
     * 向uartservice发送带value值的指令
     * @param cmd   指令
     * @param value  值
     */
    public static void sendCmdToUartService(Context context, int cmd, int value){
        Intent intent =new Intent("com.demo.ibotncamera.SEND.UART.CMD.WITHVALUE");
        intent.putExtra("cmd",cmd);
        intent.putExtra("value",value);
        context.sendBroadcast(intent);
    }
}
