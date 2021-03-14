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
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>
#include <SLES/OpenSLES_AndroidMetadata.h>
#include <unistd.h> // sleep 的头文件
#include <android/log.h>
#define LOGD(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"ywl5320",FORMAT,##__VA_ARGS__);

//1 创建引擎
static SLObjectItf  engineSL = NULL;
static SLObjectItf mix = NULL;//混音器
static SLObjectItf player = NULL;//播放器
/* 暂停 */
static bool isPaused = false;
static FILE *fp = NULL;
static char *buf = NULL;
SLEngineItf CreateSL()
{
    SLresult re;
    SLEngineItf en;
    re = slCreateEngine(&engineSL,0,0,0,0,0);
    if(re != SL_RESULT_SUCCESS) return NULL;
    re = (*engineSL)->Realize(engineSL,SL_BOOLEAN_FALSE);
    if(re != SL_RESULT_SUCCESS) return NULL;
    re = (*engineSL)->GetInterface(engineSL,SL_IID_ENGINE,&en);
    if(re != SL_RESULT_SUCCESS) return NULL;
    return en;
}

void PcmCall(SLAndroidSimpleBufferQueueItf bf,void *contex)
{
//    LOGD("PcmCall");
    if(!buf)
    {
        buf = new char[1024*1024];
    }
    if(!fp)
    {
        fp = fopen("/sdcard/testing/test.pcm","rb");
    }
    if(!fp)return;
    if(feof(fp) == 0)
    {
        int len = fread(buf,1,1024,fp);
        if(len > 0){
            while (isPaused){
                sleep(1);
            }
            (*bf)->Enqueue(bf,buf,len);
        } else{//关闭文件，重置变量，释放资源，便于下次能重头正常播放
            fclose(fp);
            fp = NULL;
            free(buf);
            buf = NULL;
        }
    } else{//关闭文件，重置变量，释放资源，便于下次能重头正常播放
        LOGD("PcmCall,end of file.");
        fclose(fp);
        fp = NULL;
        free(buf);
        buf = NULL;

        /* Destroy the player */
        if(player != NULL)
            (*player)->Destroy(player);
        /* Destroy Output Mix object */
        if(mix != NULL)
            (*mix)->Destroy(mix);
        if(engineSL != NULL)
            (*engineSL)->Destroy(engineSL);
    }

}

extern "C"
JNIEXPORT jstring

JNICALL
Java_aplay_testopensl_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_aplay_testopensl_MainActivity_startPlay(JNIEnv *env, jobject thiz) {
    // TODO: implement startPlay()

    //1 创建引擎
    SLEngineItf eng = CreateSL();
    if(eng){
        LOGD("CreateSL success！ ");
    }else{
        LOGD("CreateSL failed！ ");
        _exit(0);
    }

    //2 创建混音器
    SLresult re = 0;
    re = (*eng)->CreateOutputMix(eng,&mix,0,0,0);
    if(re !=SL_RESULT_SUCCESS )
    {
        LOGD("SL_RESULT_SUCCESS failed!");
    }
    re = (*mix)->Realize(mix,SL_BOOLEAN_FALSE);
    if(re !=SL_RESULT_SUCCESS )
    {
        LOGD("(*mix)->Realize failed!");
    }
    SLDataLocator_OutputMix outmix = {SL_DATALOCATOR_OUTPUTMIX,mix};
    /*SLDataSink audioSink= {&outmix,0};*/ //这种写法同下面的先定义结构体变量，再对每一个成员单独赋值。
    SLDataSink audioSink;
    audioSink.pLocator = &outmix;
    audioSink.pFormat = 0;

    //3 配置音频信息
    //缓冲队列
    //缓冲队列使用{SLDataLocator_BufferQueue}{SLDataLocator_AndroidSimpleBufferQueue},验证在android平台上都是可以的。只是定义buffer。
    SLDataLocator_AndroidSimpleBufferQueue que = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,10};
    /*SLDataLocator_AndroidBufferQueue que = {SL_DATALOCATOR_ANDROIDBUFFERQUEUE,10};*/ //这个是不允许的
    /*SLDataLocator_BufferQueue que = {SL_DATALOCATOR_BUFFERQUEUE,10};*/
    //音频格式
    SLDataFormat_PCM pcm = {
            SL_DATAFORMAT_PCM,
            2,//    声道数
            SL_SAMPLINGRATE_44_1,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_SPEAKER_FRONT_LEFT|SL_SPEAKER_FRONT_RIGHT,
            SL_BYTEORDER_LITTLEENDIAN //字节序，小端
    };
    SLDataSource ds = {&que,&pcm};


    //4 创建播放器
    SLPlayItf iplayer = NULL;
    SLAndroidSimpleBufferQueueItf pcmQue = NULL;
    SLAndroidSimpleBufferQueueState state;
    const SLInterfaceID ids[] = {SL_IID_BUFFERQUEUE};
    const SLboolean req[] = {SL_BOOLEAN_TRUE};
    LOGD("sizeof(ids) = %d,sizeof(SLInterfaceID) = %d",sizeof(ids),sizeof(SLInterfaceID));
    re = (*eng)->CreateAudioPlayer(eng,&player,&ds,&audioSink,sizeof(ids)/sizeof(SLInterfaceID),ids,req);
    if(re !=SL_RESULT_SUCCESS )
    {
        LOGD("CreateAudioPlayer failed!");
    } else{
        LOGD("CreateAudioPlayer success!");
    }
    (*player)->Realize(player,SL_BOOLEAN_FALSE);
    //获取player接口
    re = (*player)->GetInterface(player,SL_IID_PLAY,&iplayer);
    if(re !=SL_RESULT_SUCCESS )
    {
        LOGD("GetInterface SL_IID_PLAY failed!");
    }
    re = (*player)->GetInterface(player,SL_IID_BUFFERQUEUE,&pcmQue);
    if(re !=SL_RESULT_SUCCESS )
    {
        LOGD("GetInterface SL_IID_BUFFERQUEUE failed!");
    }

    //设置回调函数，播放队列空调用
    (*pcmQue)->RegisterCallback(pcmQue,PcmCall,0);

    //设置为播放状态
    (*iplayer)->SetPlayState(iplayer,SL_PLAYSTATE_PLAYING);

    //启动队列回调
    (*pcmQue)->Enqueue(pcmQue,"",1);
}
extern "C"
JNIEXPORT void JNICALL
Java_aplay_testopensl_MainActivity_pauseOrContinue(JNIEnv *env, jobject thiz) {
    isPaused = !isPaused;
}
extern "C"
JNIEXPORT void JNICALL
Java_aplay_testopensl_MainActivity_stopPlay(JNIEnv *env, jobject thiz) {

    LOGD("stopPlay.");
    fclose(fp);
    fp = NULL;
    free(buf);
    buf = NULL;
    /* Destroy the player */
    if(player != NULL)
        (*player)->Destroy(player);
    /* Destroy Output Mix object */
    if(mix != NULL)
        (*mix)->Destroy(mix);
    if(engineSL != NULL)
        (*engineSL)->Destroy(engineSL);
}
extern "C"
JNIEXPORT void JNICALL
Java_aplay_testopensl_MainActivity_stopAndExit(JNIEnv *env, jobject thiz) {
    LOGD("stopPlay.");
    fclose(fp);
    fp = NULL;
    free(buf);
    buf = NULL;
    /* Destroy the player */
    if(player != NULL)
        (*player)->Destroy(player);
    /* Destroy Output Mix object */
    if(mix != NULL)
        (*mix)->Destroy(mix);
    if(engineSL != NULL)
        (*engineSL)->Destroy(engineSL);
    _exit(0);
}