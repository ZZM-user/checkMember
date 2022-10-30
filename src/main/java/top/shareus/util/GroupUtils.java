package top.shareus.util;

import cn.hutool.core.util.ObjectUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import top.shareus.CheckMember;
import top.shareus.common.constant.GroupsConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组 常用的工具类
 *
 * @author 17602
 * @date 2022/10/29 17:17
 */
public class GroupUtils {
    
    
    /**
     * 得到当前机器人下 所有小组成员（全部：为某业务场景定制）
     *
     * @return {@link Map}<{@link String}, {@link ContactList}<{@link NormalMember}>>
     */
    public static Map<String, ContactList<NormalMember>> getAllGroupMembers(Bot bot) {
        Map<String, ContactList<NormalMember>> contactListMap = new HashMap<>();
        
        // 读取 管理组成员
        ContactList<NormalMember> adminMemberList = GroupUtils.getGroupMembers(bot, GroupsConstant.ADMIN_GROUPS);
        contactListMap.put("admin", adminMemberList);
        
        // 读取 资源组成员
        ContactList<NormalMember> resMemberList = GroupUtils.getGroupMembers(bot, GroupsConstant.RES_GROUPS);
        contactListMap.put("res", resMemberList);
        
        // 读取 聊天组成员
        ContactList<NormalMember> chatMemberList = GroupUtils.getGroupMembers(bot, GroupsConstant.CHAT_GROUPS);
        contactListMap.put("chat", chatMemberList);
        
        return contactListMap;
    }
    
    /**
     * 获取当前机器人下的指定群组成员列表
     *
     * @param bot
     * @param group
     *
     * @return
     */
    public static ContactList<NormalMember> getGroupMembers(Bot bot, List<Long> group) {
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
    public static Boolean invalidGroup
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
