package top.shareus.common;

import cn.hutool.core.util.ObjectUtil;
import net.mamoe.mirai.Bot;
import top.shareus.util.LogUtils;

/**
 * 机器人管理
 *
 * @author 17602
 * @date 2022/8/27 13:58
 */
public class BotManager {
    private static final Bot BOT = Bot.getInstances().get(0);
    
    public static Bot getBot() {
        if (ObjectUtil.isNull(BOT)) {
            LogUtils.error("没有机器人正在运行");
            throw new RuntimeException("没有机器人正在运行");
        }
        return BOT;
    }
}
