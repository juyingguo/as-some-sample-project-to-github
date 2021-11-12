package com.ibotn.wifitransfer.utils;

import android.content.Context;
import android.content.Intent;

/**
 * create by jy on 2018/10/8 ,9:32
 * des:tts utils (语音tts)
 */
public class TtsUtils {
    /**
     * 外部tts权重值，其它条件相同的情况下权重大的tts,会中断权重小的tts，外部tts权重起始值为5
     */
    public static final int TTS_WEIGHT_5 = 5;
    /** 外部广播事件启动tts */
    public  static final String TTS_EXT_REQUEST = "com.ibotn.ibotnvoice.TTS_EXT_REQUEST";
    /**
     * 外部广播事件启动tts <br/>
     * @param context Context
     * @param intent tts 广播intent
     * @return success true;otherwise false
     */
    public static boolean startTtsSpeaker(Context context, Intent intent) {
        if (context == null || intent == null){
            return false;
        }
        context.sendBroadcast(intent);
        return true;
    }

    /**
     * 外部广播事件启动tts <br/>
     * @param context 上下文
     * @param content 语音合成内容
     * @param weight 权重
     * @return success true;otherwise false
     */
    public static boolean startTtsSpeaker(Context context, String content, int weight) {
        if (content == null || content.length() == 0){
            return false;
        }
        Intent intent = new Intent(TTS_EXT_REQUEST);
        intent.putExtra("content", content);
        intent.putExtra("weight", weight);
        context.sendBroadcast(intent);
        return true;
    }
}
