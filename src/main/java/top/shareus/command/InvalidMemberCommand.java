package top.shareus.command;

import cn.hutool.core.util.ObjectUtil;
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
import top.shareus.common.constant.GroupsConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： 17602
 * @Date： 2022/8/27 19:55
 * @Desc： 筛选无效成员
 **/
public class InvalidMemberCommand extends JRawCommand {
    public static final InvalidMemberCommand INSTANCE = new InvalidMemberCommand();
    
    private InvalidMemberCommand() {
        // 使用插件主类对象作为指令拥有者；设置主指令名为 "test"
        super(CheckMember.INSTANCE, "invalidMember");
        // 可选设置如下属性
        // setPermission();
        // 设置用法，这将会在 /help 中展示
        setUsage("/invalidMember");
        // 设置描述，也会在 /help 中展示
        setDescription("获取N个群中的失效人员");
        // 设置指令前缀是可选的，即使用 `test` 也能执行指令而不需要 `/test`
        setPrefixOptional(true);
    }
    
    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        Bot bot = BotManager.getBot();
        CommandSender sender = context.getSender();
    
        ContactList<NormalMember> adminMemberList = parseGroup(bot, GroupsConstant.ADMIN_GROUPS);
        ContactList<NormalMember> resMemberList = parseGroup(bot, GroupsConstant.RES_GROUPS);
        ContactList<NormalMember> chatMemberList = parseGroup(bot, GroupsConstant.CHAT_GROUPS);
    
        CheckMember.INSTANCE.getLogger().debug(adminMemberList.size() + "\t" + resMemberList.size() + "\t" + chatMemberList.size());
        Boolean hasGroups = hasGroups(adminMemberList, resMemberList, chatMemberList);
        try {
            if (hasGroups) {
                ContactList<NormalMember> invalidMember = getInvalidMember(adminMemberList, resMemberList, chatMemberList);
                String formatGroupMember = GroupCommand.formatGroupMember("失效人员列表", invalidMember);
                sender.sendMessage(formatGroupMember);
            }
        } catch (Exception e) {
            CheckMember.INSTANCE.getLogger().error(e);
            sender.sendMessage("操作失败，请联系管理员！");
        }
    
    }
    
    /**
     * 获取失效的群成员
     *
     * @param adminMemberList
     * @param resMemberList
     * @param chatMemberList
     *
     * @return
     */
    private ContactList<NormalMember> getInvalidMember(ContactList<NormalMember> adminMemberList, ContactList<NormalMember> resMemberList, ContactList<NormalMember> chatMemberList) {
        List<NormalMember> invalidMember = new ArrayList<>();
        for (NormalMember member : resMemberList) {
            long id = member.getId();
            NormalMember adminMember = adminMemberList.stream().filter(m -> m.getId() == id).findAny().orElse(null);
            NormalMember chatMember = chatMemberList.stream().filter(m -> m.getId() == id).findAny().orElse(null);
            if (ObjectUtil.isNull(adminMember) && ObjectUtil.isNull(chatMember)) {
                invalidMember.add(member);
            }
        }
        return new ContactList<>(invalidMember);
    }
    
    /**
     * 获取当前机器人下的指定群组成员列表
     *
     * @param bot
     * @param group
     *
     * @return
     */
    private ContactList<NormalMember> parseGroup(Bot bot, List<Long> group) {
        List<NormalMember> groupList = new ArrayList<>();
        Group groupTemp;
        for (Long number : group) {
            groupTemp = bot.getGroup(number);
            if (ObjectUtil.isNotNull(groupTemp)) {
                groupList.addAll(groupTemp.getMembers());
                CheckMember.INSTANCE.getLogger().debug(groupTemp.getName() + "\n" + groupTemp.getMembers().size() + "成员");
            } else {
                CheckMember.INSTANCE.getLogger().error("获取群成员列表失败：" + number);
            }
        }
        return new ContactList<>(groupList);
    }
    
    /**
     * 检查必备的群是否加载成功
     *
     * @param adminMemberList
     * @param resMemberList
     * @param chatMemberList
     *
     * @return
     */
    private Boolean hasGroups
    (ContactList<NormalMember> adminMemberList, ContactList<NormalMember> resMemberList, ContactList<NormalMember> chatMemberList) {
        if (adminMemberList.isEmpty()) {
            CheckMember.INSTANCE.getLogger().error("管理群组加载失败！");
        }
        if (resMemberList.isEmpty()) {
            CheckMember.INSTANCE.getLogger().error("资源群组加载失败！");
            return false;
        }
        if (chatMemberList.isEmpty()) {
            CheckMember.INSTANCE.getLogger().error("聊天群组加载失败！");
            return false;
        }
        return true;
    }
}
