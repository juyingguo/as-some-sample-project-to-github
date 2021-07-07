package com.demo.camera.beans;

import android.text.TextUtils;
import android.util.Log;

import com.demo.camera.utils.LearnTrajectoryUtil;

import java.lang.reflect.Field;

/**
 * Created by phc on 2017/7/14 0014.
 * 发送广播时 学习轨迹实体类
 */

public class LearnTrajectoryBean {
    private final String TAG = getClass().getSimpleName();
    private long startTime;
    private String name;
    private long endTime;
    private int trackType;

    public LearnTrajectoryBean(long startTime, String name, @LearnTrajectoryUtil.TypeConstant int trackType) {
        this.startTime = startTime;
        this.name = name;
        this.trackType = trackType;
    }

    public LearnTrajectoryBean() {
    }

    public int getTrackType() {
        return trackType;
    }

    public void setTrackType(int trackType) {
        this.trackType = trackType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("{");
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field :
                    fields) {
                if (TextUtils.equals(field.getName(), "TAG") ||
                        TextUtils.equals(field.getName(), "$change") ||
                        TextUtils.equals(field.getName(), "serialVersionUID"))
                    continue;
                field.setAccessible(true);
                sb.append("\"")
                        .append(field.getName())
                        .append("\":");
                if (field.getType() == String.class) {
                    sb.append("\"")
                            .append(field.get(this))
                            .append("\",");
                } else {
                    sb.append(field.get(this))
                            .append(",");
                }

            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append("}");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Log.i(TAG, sb.toString());
        return sb.toString();
    }
}
