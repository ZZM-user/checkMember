package top.shareus.common;

import net.mamoe.mirai.Bot;
import top.shareus.util.LogUtils;

import java.util.List;

/**
 * @Author： 17602
 * @Date： 2022/8/27 13:58
 * @Desc： 机器人管理
 **/
public class BotManager {
    private static final String NOT_BOT = "没有机器人正在运行";
    
    public static Bot getBot() {
        Bot bot;
        List<Bot> instances = Bot.getInstances();
        if (instances.size() != 0) {
            LogUtils.debug("获取到机器人实例");
            bot = instances.get(0);
        } else {
            LogUtils.error(NOT_BOT);
            throw new RuntimeException(NOT_BOT);
        }
        
        return bot;
    }
}
