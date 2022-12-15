package top.shareus.common.core.constant;

import cn.hutool.core.date.LocalDateTimeUtil;

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
     * 求文key 到期时间
     */
    public static final Long REDIS_KEY_EXPIRE = 24 * 60 * 60L;

    /**
     * 计算到期时间
     *
     * @return {@link Long}
     */
    public static Long getExpireTime() {
        return REDIS_KEY_EXPIRE - LocalDateTimeUtil.now().getHour() * 60 * 60;
    }
}
