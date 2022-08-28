package top.shareus.command;

import cn.hutool.core.date.DateTime;
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
            CheckMember.INSTANCE.getLogger().error(message);
            sender.sendMessage(message);
            return;
        }
        ContactList<NormalMember> members = group.getMembers();
        String formatGroupMember = formatGroupMember("群成员列表", members);
        CheckMember.INSTANCE.getLogger().info(args.get(0) + "\t请求到了：" + members.size() + "名成员的信息");
        
        sender.sendMessage(formatGroupMember);
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
            temp = ++index
                           // qq
                           + "-" + member.getId()
                           // 备注
                           + "-备注：" + member.getRemark()
                           // 群名片
                           + "-群名片：" + member.getNameCard()
                           // 所属群组名称
                           + "-所在群组：" + member.getGroup().getName()
                           // 所属群组
                           + "-所在群组号码：" + member.getGroup().getId()
                           // 特殊头衔
                           + "-头衔：" + member.getSpecialTitle()
                           // // 头像
                           // + "-头像：" + member.getAvatarUrl()
                           // 是否禁言
                           + "-" + (member.isMuted() ? "禁言中" : "未禁言")
                           // 秒级时间戳 * 1000 = 毫秒级时间戳
                           // 剩余禁言时长
                           + "-" + (member.isMuted() ? DateTime.of(member.getMuteTimeRemaining() * 1000L) : "无禁言")
                           // 最后发言时间
                           + "-最后发言时间：" + DateTime.of(member.getLastSpeakTimestamp() * 1000L)
                           // 进群时间
                           + "-进群时间：" + DateTime.of(member.getJoinTimestamp() * 1000L)
                           + '\n';
            builder.append(temp);
        }
        builder.append("本群总人数：").append(index);
        return builder.toString();
    }
}
