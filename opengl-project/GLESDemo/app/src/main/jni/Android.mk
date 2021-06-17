LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := MICloudPub  
LOCAL_MODULE := libMICloudPub 

LOCAL_SRC_FILES := RenderOpenGles20.cpp test.cpp NativeYUVSurface.cpp MyDef.cpp YUVUtil.cpp

LOCAL_LDLIBS :=	-llog -lgcc -lGLESv2 -lz  
#LOCAL_CFLAGS +=  -DDEBUG		#在所有文件中定义 DEBUG 用来控制log输出
LOCAL_CPPFLAGS:=-std=c++11	
include $(BUILD_SHARED_LIBRARY)
