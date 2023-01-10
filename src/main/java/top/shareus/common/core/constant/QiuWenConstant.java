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

    public static final String ERROR_QIU_WEN_TEMPLATE_REDIS_KEY = "Error_Template: ";

    /**
     * 每天求文最大次数
     */
    public static final Long MAX_TIMES_OF_DAY = 3L;

    /**
     * 每周求文规范错误最大次数
     */
    public static final Long ERROR_TEMPLATE_MAX_TIMES_OF_WEEK = 5L;

    /**
     * 求文日志多久会自动失败
     */
    public static final Long QIU_WEN_MAX_DAY_WILL_FAIL = 3L;


    /**
     * 计算求文到期时间
     *
     * @return {@link Long}
     */
    public static Long getExpireTime() {
        // 当天的结束时间戳 - 现在的时间戳
        return DateUtil.between(DateTime.now(), DateUtil.endOfDay(new Date()), DateUnit.SECOND, true);
    }

    /**
     * 计算规范错误到期时间
     *
     * @return {@link Long}
     */
    public static Long getExpireTimeOfErrorTemplate() {
        // 当天的结束时间戳 - 现在的时间戳
        return DateUtil.between(DateTime.now(), DateUtil.nextWeek(), DateUnit.SECOND, true);
    }
}
