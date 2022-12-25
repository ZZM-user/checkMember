package top.shareus.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

class QiuWenConstantTest {

    @org.junit.jupiter.api.Test
    void getExpireTime() {
        long between = DateUtil.between(DateTime.now(), DateUtil.endOfDay(new Date()), DateUnit.SECOND, true);
        System.out.println(between);
        System.out.println(24.0 - (between / 60.0 / 60.0));
        System.out.println(DateTime.now() + "\n" + DateUtil.endOfDay(new Date()));
    }
}