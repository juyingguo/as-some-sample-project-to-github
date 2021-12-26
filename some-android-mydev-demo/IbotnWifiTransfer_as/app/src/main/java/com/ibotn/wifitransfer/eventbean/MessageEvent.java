package com.ibotn.wifitransfer.eventbean;
/**
 * create by jy on 2018/8/7 ,13:56
 * des:
 */
public class MessageEvent {
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String filePath;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    /**
     *
     * @param message message
     * @param filePath  file path
     */
    public MessageEvent(String message,String filePath) {
        this.message = message;
        this.filePath = filePath;
    }


}