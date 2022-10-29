package top.shareus.command;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.shareus.CheckMember;
import top.shareus.common.BotManager;
import top.shareus.util.LogUtils;
import top.shareus.util.NormalMemberUtils;

import java.text.SimpleDateFormat;

/**
 * @Author： 17602
 * @Date： 2022/8/24 16:56
 * @Desc： 获取某个群的成员列表
 **/
public final class GroupCommand extends JRawCommand {
    public static final GroupCommand INSTANCE = new GroupCommand();
    
    private GroupCommand() {
        // 使用插件主类对象作为指令拥有者；设置主指令名为 "test"
        super(CheckMember.INSTANCE, "gp");
        // 可选设置如下属性
        
        // 设置用法，这将会在 /help 中展示
        setUsage("/gp <group>");
        // 设置描述，也会在 /help 中展示
        setDescription("获取指定群组的群员列表");
        // 设置指令前缀是可选的，即使用 `test` 也能执行指令而不需要 `/test`
        setPrefixOptional(true);
    }
    
    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        CommandSender sender = context.getSender();
        
        if (args.size() == 0) {
            sender.sendMessage("参数数量不正确 eg: " + getUsage() + " 123345");
            return;
        }
        Bot bot = BotManager.getBot();
        Group group = bot.getGroup(Long.parseLong(args.get(0).toString()));
        if (ObjectUtil.isNull(group)) {
            String message = "该群组成员获取失败!\n group：" + args.get(0);
            LogUtils.error(message);
            sender.sendMessage(message);
            return;
        }
        try {
            ContactList<NormalMember> members = group.getMembers();
            String formatGroupMember = formatGroupMember("群成员列表", members);
            LogUtils.info(args.get(0) + "\t请求到了：" + members.size() + "名成员的信息");
        
            sender.sendMessage(formatGroupMember);
        } catch (Exception e) {
            LogUtils.error(e);
            sender.sendMessage("操作失败，请联系管理员！");
        }
    }
    
    
    /**
     * 格式化成员列表
     *
     * @param members
     *
     * @return
     */
    static String formatGroupMember(String title, ContactList<NormalMember> members) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder builder = new StringBuilder();
        if (StrUtil.isNotBlank(title)) {
            builder.append(title).append('\n');
        }
        String temp;
        int index = 0;
        for (NormalMember member : members) {
            temp = ++index + NormalMemberUtils.format(member, false);
            builder.append(temp);
        }
        builder.append("总人数：").append(index);
        return builder.toString();
    }
}
