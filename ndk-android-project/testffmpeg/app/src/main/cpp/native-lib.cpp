/*******************************************************************************
**                                                                            **
**                     Jiedi(China nanjing)Ltd.                               **
**	               创建：夏曹俊，此代码可用作为学习参考                       **
*******************************************************************************/

/*****************************FILE INFOMATION***********************************
**
** Project       : FFmpeg
** Description   : FFMPEG项目创建示例
** Contact       : xiacaojun@qq.com
**        博客   : http://blog.csdn.net/jiedichina
**		视频课程 : 网易云课堂	http://study.163.com/u/xiacaojun		
				   腾讯课堂		https://jiedi.ke.qq.com/				
				   csdn学院		http://edu.csdn.net/lecturer/lecturer_detail?lecturer_id=961	
**                 51cto学院	http://edu.51cto.com/lecturer/index/user_id-12016059.html	
** 				   下载最新的ffmpeg版本 ffmpeg.club
**                 
**   安卓流媒体播放器 课程群 ：23304930 加入群下载代码和交流
**   微信公众号  : jiedi2007
**		头条号	 : 夏曹俊
**
*******************************************************************************/
//！！！！！！！！！ 加群23304930下载代码和交流

#include <jni.h>
#include <string>
#include <android/log.h>
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,"testff",__VA_ARGS__)

extern "C"{
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
}
#include<iostream>
using namespace std;

static double r2d(AVRational r)
{
    LOGW("r2d,r.num = %d,r.den=%d ",
         r.num,
         r.den
    );
    return r.num==0||r.den == 0 ? 0 :(double)r.num/(double)r.den;
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_aplay_testffmpeg_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ \n";
    hello += "avcodec_version = ";
    hello += avcodec_version();
    hello += "\n";
    hello += "avcodec_license()=";
    hello += avcodec_license();
    hello += "\n";
    hello += "avcodec_configuration=";
    hello += avcodec_configuration();
    //初始化解封装
    av_register_all();
    //初始化网络
    avformat_network_init();

    //打开文件
    AVFormatContext *ic = NULL;
//    char path[] = "/sdcard/paiDuiGe.mp4";
    char path[] = "/sdcard/paiDuiGe.flv";
    int re = avformat_open_input(&ic,path,0,0);
    if(re != 0)
    {
        LOGW("avformat_open_input failed!:%s",av_err2str(re));
        return env->NewStringUTF(hello.c_str());
    }
    LOGW("avformat_open_input %s success!",path);
    //获取流信息
    re = avformat_find_stream_info(ic,0);
    if(re != 0)
    {
        LOGW("avformat_find_stream_info failed!");
    }
    LOGW("duration = %lld nb_streams = %d",ic->duration,ic->nb_streams);

    int fps = 0;
    int videoStream = 0;
    int audioStream = 1;

    for(int i = 0; i < ic->nb_streams; i++)
    {
        AVStream *as = ic->streams[i];
        if(as->codecpar->codec_type == AVMEDIA_TYPE_VIDEO)
        {
            LOGW("视频数据");
            videoStream = i;
            fps = static_cast<int>(r2d(as->avg_frame_rate));

            LOGW("fps = %d,width=%d height=%d codeid=%d pixformat=%d",fps,
                 as->codecpar->width,
                 as->codecpar->height,
                 as->codecpar->codec_id,
                 as->codecpar->format
            );
        }
        else if(as->codecpar->codec_type ==AVMEDIA_TYPE_AUDIO )
        {
            LOGW("音频数据");
            audioStream = i;
            LOGW("sample_rate=%d channels=%d sample_format=%d",
                 as->codecpar->sample_rate,
                 as->codecpar->channels,
                 as->codecpar->format
            );
        }
    }
    //ic->streams[videoStream];


    //关闭上下文
    avformat_close_input(&ic);
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_aplay_testffmpeg_MainActivity_Open(JNIEnv *env, jobject instance, jstring url_,
                                        jobject handle) {
    const char *url = env->GetStringUTFChars(url_, 0);

    // TODO
    FILE *fp = fopen(url,"rb");
    if(!fp)
    {
        LOGW("File %s open failed!",url);
    }
    else
    {
        LOGW("File %s open succes!",url);
        fclose(fp);
    }
    env->ReleaseStringUTFChars(url_, url);
    return true;
}