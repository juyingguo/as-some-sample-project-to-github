package com.example.liuwangshu.moonmvpsimple.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtil {

    private static final Gson gson = new Gson();

    /**
     * 转成 Json 字符串
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Json 转 JavaBean 对象
     */
    public static <T> T toBean(String json, Class<T> beanClass) {
        return gson.fromJson(json, beanClass);
    }

    /**
     * Json 转 List 集合
     */
    public static <T> List<T> toList(String json) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    /**
     * Json 转 List 集合
     * 遇到解析不了的，尝试使用这个
     */
    public static <T> List<T> toListExt(String json, Class<T> clz) {
        List<T> mList = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        Gson mGson = new Gson();
        for (final JsonElement elem : array) {
            mList.add(mGson.fromJson(elem, clz));
        }
        return mList;
    }


    /**
     * Json 转换成 Map 的 List 集合对象
     *
     * @param json json 字符串
     */
    public static <T> List<Map<String, T>> toListMap(String json) {
        Type type = new TypeToken<List<Map<String, T>>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Json 转 Map 对象
     *
     * @param json json 字符串
     */
    public static <T> Map<String, T> toMap(String json) {
        Type type = new TypeToken<Map<String, T>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}

