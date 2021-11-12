package com.ibotn.wifitransfer.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * create by jy on 2018/8/8 ,17:13
 * des:
 */
public class IOManagerUtils {

    /** {@value} */
    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB
    public static void close(Socket socket){
        if (socket != null)
        {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(InputStream inputStream){
        if (inputStream != null)
        {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Reader reader){
        if (reader != null)
        {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(OutputStream outputStream){
        if (outputStream != null)
        {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Writer writer){
        if (writer != null)
        {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
                if (ignored != null){
                    ignored.printStackTrace();
                }
            }
        }
    }

}