package com.example.liuwangshu.moondatabinding.util;

import android.annotation.SuppressLint;
import android.databinding.BindingConversion;

import com.example.liuwangshu.moondatabinding.model.Swordsman;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {
    public static String getName(Swordsman swordsman) {
        return swordsman.getName();
    }

    @BindingConversion
    public static String convertDate(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss.SSS");
        return sdf.format(date);
    }
}
