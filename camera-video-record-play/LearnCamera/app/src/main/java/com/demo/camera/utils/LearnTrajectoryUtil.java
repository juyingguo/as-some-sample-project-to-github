package com.demo.camera.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.demo.camera.beans.LearnTrajectoryBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static com.demo.camera.utils.LearnTrajectoryUtil.Constant.ACTION_LEARN_TRAJECTORY;


/**
 * Created by phc on 2017/7/14 0014.
 * 发送学习轨迹广播的工具类
 */

public class LearnTrajectoryUtil {
    private static final String TAG = "LearnTrajectoryUtil";

    /**
     * 发送广播到launcher 利用launcher的udp通信给手机发消息 统一出口
     *
     * @param context
     * @param bean    轨迹实体类
     */
    public static void sendBro(Context context, @NonNull LearnTrajectoryBean bean) {
        sendBro(context, "[" + bean.toString() + "]");

    }

    /**
     * 发送广播到launcher 利用launcher的udp通信给手机发消息 统一出口
     *
     * @param context
     * @param holder  轨迹holder
     */
    public static void sendBro(Context context, @NonNull LearnTrajectoryHolder holder) {

        if (holder.getTrajectory() == null || holder.getTrajectory().isEmpty()) {
            Log.i(TAG, "rajectoryList is null or empty,return ");
            return;
        }
        sendBro(context, holder.getTrajectory().toString());

    }

    /**
     * 发送广播到launcher 利用launcher的udp通信给手机发消息 统一出口
     *
     * @param context
     * @param strJson 轨迹对应的json字符串
     */
    private static void sendBro(Context context, @NonNull String strJson) {
        try {
            Log.e(TAG, "sendBro strJson is : " + strJson);
            Intent intent = new Intent();
            intent.setAction(ACTION_LEARN_TRAJECTORY)
                    .putExtra("message_type", "track")
                    .putExtra("message_description", strJson)
                    .putExtra("message_name", "track");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断app是否在前台
     *
     * @param packName 包名
     * @return true在前台 反之
     */
    public static boolean isRunFore(Context context, String packName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info :
                am.getRunningAppProcesses()) {
            if (TextUtils.equals(packName, info.processName)
                    && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    @IntDef({Constant.TYPE_CALL,
            Constant.TYPE_GROW_VIDEO,
            Constant.TYPE_GROW_PHOTO,
            Constant.TYPE_EDU_AUDIO,
            Constant.TYPE_EDU_VIDEO,
            Constant.TYPE_EDU_WAWALU,
            Constant.TYPE_PUZZLE,
            Constant.TYPE_BROWSER,
            Constant.TYPE_IDENTIFY_FOLLOW,
            Constant.TYPE_SCENE_LEARNING,
            Constant.TYPE_SMART_REMINDER,
            Constant.TYPE_FACE_RECOGNITION,
            Constant.TYPE_LOCATION_INFO,
            Constant.TYPE_BOOT_INFO,
            Constant.TYPE_SHUTDOWN_INFO,
            Constant.TYPE_FACE_CHECK_IN,
            Constant.TYPE_SMART_PHOTO,
            Constant.TYPE_NOTIFICATION_MODE,
            Constant.TYPE_VIDEO_PROJECTION_SCREEN,
            Constant.TYPE_QR_SCAN,
            Constant.TYPE_SMART_ALBUM,
            Constant.TYPE_GROWTH_ALBUM,
            Constant.TYPE_TAKE_PHOTO,
            Constant.TYPE_CHINESE_ANIMATION,
            Constant.TYPE_CHINESE_CHILDREN_SONG,
            Constant.TYPE_CHINESE_STORY,
            Constant.TYPE_TRADITIONAL_CULTURE,
            Constant.TYPE_ENGLISH_NEWS,
            Constant.TYPE_ENGLISH_NURSERY_RHYMES,
            Constant.TYPE_RIDDLE,
            Constant.TYPE_LIGHT_MUSIC,
            Constant.TYPE_MESSAGE_REMINDER,
            Constant.TYPE_ORAL_PRACTICE,
            Constant.TYPE_OPEN_VISITORS,
            Constant.TYPE_CLOSE_VISITOR,
            Constant.TYPE_ONLINE_CLASSROOM,
            Constant.TYPE_SYSTEM_SETTINGS,
            Constant.TYPE_EXTERNAL_CARD,
            Constant.TYPE_APPLICATION_SETTINGS
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypeConstant {
    }

    /*   *//**
     * 记录学习轨迹的接口
     *//*
    public interface LearnTrajectoryImpl {
        *//**
     * 添加学习轨迹到list中
     *
     * @param bean
     *//*
        void addTrajectory(LearnTrajectoryBean bean);

        */

    /**
     * 获取储存轨迹的list
     *
     * @return
     *//*
        ArrayList getTrajectory();
    }
*/
    public static class LearnTrajectoryHolder {
        private ArrayList<LearnTrajectoryBean> trajectoryList = new ArrayList<>();
        /**
         * 是否进入学习
         */
        private boolean isStart = false;
        /**
         * 间隔时间
         */
        private int timeSpace = 5000;
        /**
         * 开始时间
         */
        private long startTime;
        /**
         * 结束时间
         */
        private long endTime;

        private LearnTrajectoryBean tempBean;

        public void startAndEndLearn(LearnTrajectoryBean bean) {
            bean.setEndTime(bean.getStartTime());
            trajectoryList.add(bean);
            tempBean = null;
        }

        /**
         * 添加学习轨迹到list中
         *
         * @param bean
         */
        private void addTrajectory(LearnTrajectoryBean bean) {
            if (bean != null && endTime - startTime >= timeSpace) {
                bean.setEndTime(endTime);
                trajectoryList.add(bean);
                tempBean = null;
            }
        }

        /**
         * 获取储存轨迹的list
         *
         * @return
         */
        public ArrayList<LearnTrajectoryBean> getTrajectory() {
            return trajectoryList;
        }

        /**
         * 设置间隔时间 单位毫秒
         *
         * @param timeSpace
         */
        public void setTimeSpace(int timeSpace) {
            this.timeSpace = timeSpace;
        }

        /**
         * 设置进入某项学习
         */
        public void startLearn(LearnTrajectoryBean bean) {
            isStart = true;
            startTime = System.currentTimeMillis();
            tempBean = bean;
        }

        /**
         * 设置结束某项学习
         */
        public void endLearn() {
            isStart = false;
            endTime = System.currentTimeMillis();
            addTrajectory(tempBean);
        }

    }

    public static class Constant {
        /**
         * 通话
         */
        public static final int TYPE_CALL = 0;
        /**
         * 成长视频
         */
        public static final int TYPE_GROW_VIDEO = 1;
        /**
         * 成长相片
         */
        public static final int TYPE_GROW_PHOTO = 2;
        /**
         * 教育音乐
         */
        public static final int TYPE_EDU_AUDIO = 3;
        /**
         * 教育视频
         */
        public static final int TYPE_EDU_VIDEO = 4;
        /**
         * 娃娃路
         */
        public static final int TYPE_EDU_WAWALU = 5;

        /**
         * 益智游戏
         */
        public static final int TYPE_PUZZLE = 6;
        /**
         * 浏览器
         */
        public static final int TYPE_BROWSER = 7;
        /**
         * 识别跟随
         */
        public static final int TYPE_IDENTIFY_FOLLOW = 8;
        /**
         * 场景学习
         */
        public static final int TYPE_SCENE_LEARNING = 9;
        /**
         * 智能提醒
         */
        public static final int TYPE_SMART_REMINDER = 10;
        /**
         * 人脸识别
         */
        public static final int TYPE_FACE_RECOGNITION = 11;

        /**
         * 位置信息
         */
        public static final int TYPE_LOCATION_INFO = 12;
        /**
         * 开机
         */
        public static final int TYPE_BOOT_INFO = 13;

        /**
         * 关机
         */
        public static final int TYPE_SHUTDOWN_INFO = 14;

        /**
         * 宝宝签到
         */
        public static final int TYPE_FACE_CHECK_IN = 16;


        /**
         * 智能抓拍
         */
        public static final int TYPE_SMART_PHOTO = 17;


        /**
         * 通知模式
         */
        public static final int TYPE_NOTIFICATION_MODE = 18;


        /**
         * 影音投屏
         */
        public static final int TYPE_VIDEO_PROJECTION_SCREEN = 19;


        /**
         * 扫码阅读
         */
        public static final int TYPE_QR_SCAN = 20;

        /**
         * 智能相册
         */
        public static final int TYPE_SMART_ALBUM = 21;

        /**
         * 成长相册
         */
        public static final int TYPE_GROWTH_ALBUM = 22;

        /**
         * 相机
         */
        public static final int TYPE_TAKE_PHOTO = 23;

        /**
         * 汉语动画
         */
        public static final int TYPE_CHINESE_ANIMATION = 24;

        /**
         * 中文儿歌
         */
        public static final int TYPE_CHINESE_CHILDREN_SONG = 25;

        /**
         * 中文故事
         */
        public static final int TYPE_CHINESE_STORY = 26;

        /**
         * 传统文化
         */
        public static final int TYPE_TRADITIONAL_CULTURE = 27;

        /**
         * 英语动态
         */
        public static final int TYPE_ENGLISH_NEWS = 28;

        /**
         * 英文儿歌
         */
        public static final int TYPE_ENGLISH_NURSERY_RHYMES = 29;

        /**
         * 谜语
         */
        public static final int TYPE_RIDDLE = 30;

        /**
         * 轻音乐
         */
        public static final int TYPE_LIGHT_MUSIC = 31;

        /**
         * 留言提醒
         */
        public static final int TYPE_MESSAGE_REMINDER = 32;

        /**
         * 口语练习
         */
        public static final int TYPE_ORAL_PRACTICE = 33;

        /**
         * 打开访客
         */
        public static final int TYPE_OPEN_VISITORS = 34;

        /**
         * 关闭访客
         */
        public static final int TYPE_CLOSE_VISITOR = 35;

        /**
         * 在线课堂
         */
        public static final int TYPE_ONLINE_CLASSROOM = 36;


        /**
         * 应用设置
         */
        public static final int TYPE_APPLICATION_SETTINGS = 37;

        /**
         * 外置卡
         */
        public static final int TYPE_EXTERNAL_CARD = 38;


        /**
         * 系统设置
         */
        public static final int TYPE_SYSTEM_SETTINGS = 39;

        /**
         * 向launcher发送的广播名
         */
        public static final String ACTION_LEARN_TRAJECTORY = "MessageDefine.LEARN_TRAJECTORY";

    }
}
