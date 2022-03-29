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


//
// Created by Administrator on 2018-03-07.
//

#include "IPlayer.h"
#include "IDemux.h"
#include "IDecode.h"
#include "IAudioPlay.h"
#include "IVideoView.h"
#include "IResample.h"
#include "XLog.h"

IPlayer *IPlayer::Get(unsigned char index)
{
    static IPlayer p[256];
    return &p[index];
}

void IPlayer::Main()
{
    while (!isExit)
    {
        mux.lock();
        if(!audioPlay|| !vdecode)
        {
            mux.unlock();
            XSleep(2);
            continue;
        }

        //同步
        //获取音频的pts 告诉视频
        int apts = audioPlay->pts;
//        XLOGE("IPlayer::Main apts = %d",apts);
        vdecode->synPts = apts;

        mux.unlock();
        XSleep(2);
    }
}
void IPlayer::setHardDecode(bool isHardDecode)
{
    this->isHardDecode = isHardDecode;
}
/** 关闭播放器，特别注意先后顺序不能错，先停止主体线程，再清理观察者，然后再清除缓冲队列，最后清理资源*/
void IPlayer::Close()
{
    mux.lock();
    //1 先关闭主体线程，再清理观察者
    //同步线程
    XThread::Stop();//当前线程停止
    //解封装
    if(demux)
        demux->Stop();
    //解码
    if(vdecode)
        vdecode->Stop();
    if(adecode)
        adecode->Stop();
    //2 清理缓冲队列
    if(vdecode)
        vdecode->Clear();
    if(adecode)
        adecode->Clear();
    if(audioPlay)
        audioPlay->Clear();

    //3 清理资源
    if(audioPlay)
        audioPlay->Close();
    if(videoView)
        videoView->Close();
    if(vdecode)
        vdecode->Close();
    if(adecode)
        adecode->Close();
    if(demux)
        demux->Close();
    mux.unlock();


}
bool IPlayer::Open(const char *path)
{
    Close();
    mux.lock();
    //解封装
    if(!demux || !demux->Open(path))
    {
        mux.unlock();
        XLOGE("demux->Open %s failed!",path);
        return false;
    }
    //解码 解码可能不需要，如果是解封之后就是原始数据
    if(!vdecode || !vdecode->Open(demux->GetVPara(),isHardDecode))
    {
        XLOGE("vdecode->Open %s failed!",path);
        //return false;
    }
    if(!adecode || !adecode->Open(demux->GetAPara()))
    {
        XLOGE("adecode->Open %s failed!",path);
        //return false;
    }

    //重采样 有可能不需要，解码后或者解封后可能是直接能播放的数据
//    if(outPara.sample_rate <= 0)
        outPara = demux->GetAPara();
    if(!resample || !resample->Open(demux->GetAPara(),outPara))
    {
        XLOGE("resample->Open %s failed!",path);
    }
    mux.unlock();
    return true;
}

bool IPlayer::Start()
{
    mux.lock();
    /**
     * 播放时，先出声音，画面是黑色的，过一会才出现画面。
     * 解决方法：
     * IPlayer::Start中调整为：先启动音频播放器，再依次启动视频解码，音频解码，解封装,同步线程。
     */
    if(audioPlay)
        audioPlay->StartPlay(outPara);
    if(vdecode)
        vdecode->Start();
    if(adecode)
        adecode->Start();
    if(!demux || !demux->Start())
    {
        mux.unlock();
        XLOGE("demux->Start failed!");
        return false;
    }
    XThread::Start();
    mux.unlock();

    return true;
}

void IPlayer::InitView(void *win)
{
    if(videoView)
    {
        videoView->Close();
        videoView->SetRender(win);
    }
}

