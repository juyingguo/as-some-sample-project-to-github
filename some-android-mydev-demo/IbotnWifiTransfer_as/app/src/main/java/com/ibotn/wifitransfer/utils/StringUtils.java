package com.ibotn.wifitransfer.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by jy on 2017/12/6.
 */
public class StringUtils {
    /**
     * 第一字符串中的所有字符是包含在第二个字符中
     */
    public static boolean containsAllChars(String first,String second){

        if (first == null || second == null){
            return false;
        }
        char[] chars = first.toCharArray();
        for (char c:
             chars) {
            if (!second.contains(c + "")){
                return false;
            }
        }
        return true;
    }
    /**
     * 将Object集合转换成字符串
     * @param obj
     * @return
     * @throws IOException
     */
    public static String object2String(Object obj)/* throws IOException */{
        String listString = null;
        try {
            // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // 然后将得到的字符数据装载到ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            objectOutputStream.writeObject(obj);

            listString = new String(byteArrayOutputStream.toByteArray());
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listString;
    }

    /**
     * 将字符串转换成压缩时的对象
     * @param <T>
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T string2Object(String objString){

        T obj = null;
        try {
            byte[] mobileBytes = objString.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    mobileBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            obj = (T) objectInputStream.readObject();
            byteArrayInputStream.close();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * @author jy
     * @param charsetName
     * @return true 为空
     * 字符串是否为空
     */
    public static boolean isSpace(String charsetName) {

        return TextUtils.isEmpty(charsetName);
    }
}
