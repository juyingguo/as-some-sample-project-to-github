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

#include "FFPlayerBuilder.h"
#include "FFDemux.h"
#include "FFdecode.h"
#include "FFResample.h"
#include "GLVideoView.h"
#include "SLAudioPlay.h"

IDemux *FFPlayerBuilder::CreateDemux()
{
    IDemux *ff = new FFDemux();
    return ff;
}

IDecode *FFPlayerBuilder::CreateDecode()
{
    IDecode *ff = new FFDecode();
    return ff;
}

IResample *FFPlayerBuilder::CreateResample()
{
    IResample *ff = new FFResample();
    return ff;
}

IVideoView *FFPlayerBuilder::CreateVideoView()
{
    IVideoView *ff = new GLVideoView();
    return ff;
}

IAudioPlay *FFPlayerBuilder::CreateAudioPlay()
{
    IAudioPlay *ff = new SLAudioPlay();
    return ff;
}

IPlayer *FFPlayerBuilder::CreatePlayer(unsigned char index)
{
    return IPlayer::Get(index);
}
/** 初始化硬解码(是和视频硬解码{ IPlayer#isHardDecode }对应需要设置的,只有都是硬解码时才需要设置，软解码当前调用也没影响),传递java虚拟机，这个函数的耦合是不可避免的。*/
void FFPlayerBuilder::InitHard(void *vm)
{
    FFDecode::InitHard(vm);
}