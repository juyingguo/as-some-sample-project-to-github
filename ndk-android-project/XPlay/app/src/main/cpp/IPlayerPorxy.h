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

#ifndef XPLAY_IPLAYERPORXY_H
#define XPLAY_IPLAYERPORXY_H


#include "IPlayer.h"
#include <mutex>
class IPlayerPorxy: public IPlayer
{
public:
    /**
     * IPlayerPorxy本身对象的创建，使用单例
     * @return IPlayerPorxy
     */
    static IPlayerPorxy*Get()
    {
        static IPlayerPorxy px;
        return &px;
    }
    void Init(void *vm = 0);
    /** this method should be call after {Init} method and before {Open} method*/
    virtual void setHardDecode(bool isHardDecode);
    virtual bool Open(const char *path);
    virtual void Close();
    virtual bool Start();
    virtual void InitView(void *win);

    //获取当前的播放进度 0.0 ~ 1.0
    virtual double PlayPos();
protected:
    IPlayerPorxy(){}
    /**
     * 这个变量，本应该通过外部注入的，当前情况，只有一路视频即一个对象，可以放在本类的内部创建。</br>
     **/
    IPlayer *player = 0;
    /**
     * 为了访问 {#player}，需要添加的互斥量
     */
    std::mutex mux;
};


#endif //XPLAY_IPLAYERPORXY_H
