package com.ibotn.wifitransfer.utils;

import java.io.File;
/**
 * create by jy on 2018/8/8 ,14:23
 * des:
 */
public class IbotnConstants {

    public static class SPContants {
        public static final String SP_NAME = "IBOTN_SP";
        public static final String SP_USER_ENTITY = "SP_USER_ENTITY";
        public static final String SP_USER_PHONE = "SP_USER_PHONE";
        public static final String SP_USER_HEAD_IMG = "SP_USER_HEAD";
        public static final String SP_CHECK_PHONE = "SP_CHECK_PHONE";
    }

    public static class StoragePathConstants {

        /**
         * 开放给用户的目录
         */
        private static final String DIR_OPEN_TO_USER = File.separator + "DIR_OPEN_TO_USER" + File.separator;
        /**
         * wifi文件传输，接收文件存放的根目录，上一层目录为优盘优先，外置ext-sd次之。内部sdcard禁止存放
         */
        private static final String DIRECTORY_WIFITRANSFER_RECEIVE = DIR_OPEN_TO_USER + "wifitransfer_receive" + File.separator;
        public static final String DIRECTORY_IMAGE= DIRECTORY_WIFITRANSFER_RECEIVE + "image" + File.separator;
        public static final String DIRECTORY_AUDIO =DIRECTORY_WIFITRANSFER_RECEIVE + "audio" + File.separator;
        public static final String DIRECTORY_VIDEO =DIRECTORY_WIFITRANSFER_RECEIVE + "video" + File.separator;
        public static final String DIRECTORY_DOCUMENT= DIRECTORY_WIFITRANSFER_RECEIVE + "document" + File.separator;
        public static final String DIRECTORY_OTHER= DIRECTORY_WIFITRANSFER_RECEIVE + "other" + File.separator;

    }
    public enum RequestCode {
        REQUEST_CODE_SELECT_PHOTO,
        REQUEST_CODE_SELECT_CLASSES,
        REQUEST_CODE_SELECT_FILE,
        REQUEST_CODE_PERMISSION,
        REQUEST_CODE_TAKE_PHOTO_CAREMA,
    }
    public enum StateConstants{
        /**
         * 未连接
         */
        UN_CONNECT,
        /**
         * 正在连接
         */
        CONNECTING,
        /**
         * 已连接
         */
        CONNECTED,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 文件发送失败
         */
        SEND_FILE_ERROR,
        /**
         * 文件发送成功
         */
        SEND_FILE_SUCCESS,
        /**
         * 文件接收成功
         */
        RECEIVE_FILE_SUCCESS,
        /**
         * 检测服务端ip设备有改变
         */
        DETECT_DEVICE_SERVER_IP_CHANGE
    }
    public enum CommandConstant{
        /**
         * 手机端发送文件，ibotn主机设备接收文件
         */
        MOBILE_CLIENT_SEND_IBOTN_DEVICE_RECEIVE_FILE,
        /**
         * ibotn主机设备发送文件，手机端接收文件
         */
        IBOTN_DEVICE_SEND_MOBILE_CLIENT_RECEIVE_FILE,
    }

    /**
     * Extra Constants
     */
    public class ExtraConstants{
        public static final String EXTRA_RESULT_LIST = "EXTRA_RESULT_LIST";
    }
}
