package top.shareus.common.core.constant;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 求文 常量
 *
 * @author zhaojl
 * @date 2022/11/27
 */
public class QiuWenConstant {

    /**
     * 求文Redis键
     */
    public static final String QIU_WEN_REDIS_KEY = "QiuWen: ";

    /**
     * 每天求文最大次数
     */
    public static final Long MAX_TIMES_OF_DAY = 5L;

    /**
     * 计算到期时间
     *
     * @return {@link Long}
     */
    public static Long getExpireTime() {
        // 当天的结束时间戳 - 现在的时间戳
        return DateUtil.between(DateTime.now(), DateUtil.endOfDay(new Date()), DateUnit.SECOND, true);
    }
}
