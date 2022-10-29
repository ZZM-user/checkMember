package top.shareus.util;

import top.shareus.CheckMember;

/**
 * 日志常用工具
 *
 * @Author： 17602
 * @Date： 2022/10/29 17:27
 */
public class LogUtils {
    
    /**
     * 信息
     *
     * @param message 消息
     */
    public static void info(String message) {
        CheckMember.INSTANCE.getLogger().info(message);
    }
    
    /**
     * 调试
     *
     * @param message 消息
     */
    public static void debug(String message) {
        CheckMember.INSTANCE.getLogger().debug(message);
    }
    
    /**
     * 警告
     *
     * @param message 消息
     */
    public static void warning(String message) {
        CheckMember.INSTANCE.getLogger().warning(message);
    }
    
    /**
     * 错误
     *
     * @param message 消息
     */
    public static void error(String message) {
        CheckMember.INSTANCE.getLogger().error(message);
    }
    
    /**
     * 错误
     *
     * @param throwable throwable
     */
    public static void error(Throwable throwable) {
        CheckMember.INSTANCE.getLogger().error(throwable);
    }
    
}
