package top.shareus;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.setting.Setting;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import top.shareus.command.ClearGroupMemberCommand;
import top.shareus.command.FindInfoByQQNumber;
import top.shareus.command.GroupCommand;
import top.shareus.command.InvalidMemberCommand;
import top.shareus.event.*;
import top.shareus.job.archived.Day;
import top.shareus.job.archived.Month;
import top.shareus.job.archived.Week;

/**
 * 检查成员
 *
 * @author 17602
 * @date 2022/10/30 14:46:17
 */
public final class CheckMember extends JavaPlugin {
    public static final CheckMember INSTANCE = new CheckMember();

    private CheckMember() {
        super(new JvmPluginDescriptionBuilder("top.shareus", "0.2.5")
                .name("checkMember")
                .author("Baidu")
                .info("百度定制群管插件")
                .build());
    }


    @Override
    public void onEnable() {
        // 注册指令、监听事件
        CommandManager.INSTANCE.registerCommand(GroupCommand.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(InvalidMemberCommand.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(ClearGroupMemberCommand.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(FindInfoByQQNumber.INSTANCE, true);

        GlobalEventChannel.INSTANCE.registerListenerHost(new ResChatEvent());
        GlobalEventChannel.INSTANCE.registerListenerHost(new OrderListEvent());
        GlobalEventChannel.INSTANCE.registerListenerHost(new HasMemberJoinEvent());
        GlobalEventChannel.INSTANCE.registerListenerHost(new ForwardAdminMessage());
        GlobalEventChannel.INSTANCE.registerListenerHost(new ArchivedResFile());
        GlobalEventChannel.INSTANCE.registerListenerHost(new QueryArchivedResFile());
        // 好友闪照转发 预留 不开启
        // GlobalEventChannel.INSTANCE.registerListenerHost(new ForwardFriendFlashImageMessage());

        initCronTask();
        getLogger().info(getDescription().getInfo() + " " + getDescription().getVersion() + " 已启动!");
    }

    /**
     * 初始化 cron任务
     *
     * @return {@link Setting}
     */
    private void initCronTask() {
        CronUtil.schedule("0 0 12 * * ?", (Task) () -> new Day().execute());
        CronUtil.schedule("0 22 * * 7", (Task) () -> new Week().execute());
        CronUtil.schedule("0 18 L * *", (Task) () -> new Month().execute());
//        CronUtil.schedule("0/1 * * * *", (Task) () -> new Week().execute());
        CronUtil.start();
        getLogger().info("初始化Corn任务完成！当前任务池数量：" + CronUtil.getScheduler().size());
    }

    @Override
    public void onDisable() {
        getLogger().info(getDescription().getInfo() + " 已关闭!");
    }
}
