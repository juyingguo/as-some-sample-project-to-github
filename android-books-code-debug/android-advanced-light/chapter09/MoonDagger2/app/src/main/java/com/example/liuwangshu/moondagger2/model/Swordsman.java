package com.example.liuwangshu.moondagger2.model;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class Swordsman {
    @Inject
// 定义了SwordsmanModule，此处不写@Inject也行，component引入module即可。对于不引入module的component(此时目标类不可以注入Swordsman，编译通不过)
// 定义了SwordsmanModule，此处写@Inject了，可以供不引入module的component使用(此时目标类才可以注入Swordsman)。
    public Swordsman(){

    }
    public String fighting() {

        return "欲为大树，莫与草争";
    }
}
