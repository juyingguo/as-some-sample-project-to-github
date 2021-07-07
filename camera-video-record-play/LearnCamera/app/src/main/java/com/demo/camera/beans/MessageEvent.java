package com.demo.camera.beans;

public class MessageEvent
{
    public static final int EVENT_TYPE_STOP_VIDEO = 2;
    public static final int EVENT_TYPE_START_VIDEO = 1;
    public MessageEvent(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int eventType ;

    @Override
    public String toString() {
        return "MessageEevent{" +
                "eventType=" + eventType +
                '}';
    }
}
