package top.shareus;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import top.shareus.command.ClearGroupMemberCommand;
import top.shareus.command.GroupCommand;
import top.shareus.command.InvalidMemberCommand;
import top.shareus.event.ForwardMessage;
import top.shareus.event.HasMemberJoinEvent;
import top.shareus.event.OrderListEvent;
import top.shareus.event.ResChatEvent;

/**
 * @author 17602
 */
public final class CheckMember extends JavaPlugin {
    public static final CheckMember INSTANCE = new CheckMember();
    
    private CheckMember() {
        super(new JvmPluginDescriptionBuilder("top.shareus", "0.1.0")
                      .name("checkMember")
                      .author("Baidu")
                      .info("百度定制群管插件")
                      .build());
    }
    
    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(GroupCommand.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(InvalidMemberCommand.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(ClearGroupMemberCommand.INSTANCE, true);
        GlobalEventChannel.INSTANCE.registerListenerHost(new ResChatEvent());
        GlobalEventChannel.INSTANCE.registerListenerHost(new OrderListEvent());
        GlobalEventChannel.INSTANCE.registerListenerHost(new HasMemberJoinEvent());
        GlobalEventChannel.INSTANCE.registerListenerHost(new ForwardMessage());
        getLogger().info("百度定制群管插件 已启动!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("百度定制群管插件 已关闭!");
    }
}
