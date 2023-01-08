package top.shareus.command;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.shareus.CheckMember;
import top.shareus.common.BotManager;
import top.shareus.common.NormalMemberVO;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.util.ExcelUtils;
import top.shareus.util.GroupUploadFileUtils;
import top.shareus.util.GroupUtils;
import top.shareus.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 筛选无效成员
 *
 * @author 17602
 * @date 2022/8/27 19:55
 */
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

        // 率先读取出所有群成员信息
        Map<String, List<NormalMemberVO>> allGroupMembers = GroupUtils.getAllGroupMembers(bot);
        List<NormalMemberVO> adminMemberList = allGroupMembers.get("admin");
        List<NormalMemberVO> resMemberList = allGroupMembers.get("res");
        List<NormalMemberVO> chatMemberList = allGroupMembers.get("chat");

        LogUtils.debug(adminMemberList.size() + "\t" + resMemberList.size() + "\t" + chatMemberList.size());
        Boolean hasGroups = GroupUtils.invalidGroup(adminMemberList, resMemberList, chatMemberList);
        try {
            if (hasGroups) {
                List<NormalMemberVO> invalidMember = getInvalidMember(adminMemberList, resMemberList, chatMemberList);
                String filePath = ExcelUtils.exportMemberDataExcel(invalidMember, "资源群失效人员名单");
                GroupUploadFileUtils.uploadFile(bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0)), filePath);
            }
        } catch (Exception e) {
            LogUtils.error(e);
            sender.sendMessage("操作失败，请联系管理员！");
        }

    }

    /**
     * 获取失效的群成员
     *
     * @param adminMemberList
     * @param resMemberList
     * @param chatMemberList
     * @return
     */
    private List<NormalMemberVO> getInvalidMember(List<NormalMemberVO> adminMemberList, List<NormalMemberVO> resMemberList, List<NormalMemberVO> chatMemberList) {
        List<NormalMemberVO> invalidMember = new ArrayList<>();
        for (NormalMemberVO member : resMemberList) {
            long id = member.getId();
            String nameCard = member.getNameCard();
            NormalMemberVO adminMember = adminMemberList.stream().filter(m -> m.getId().equals(id)).findAny().orElse(null);
            NormalMemberVO chatMember = chatMemberList.stream().filter(m -> m.getId().equals(id)).findAny().orElse(null);

            if (ObjectUtil.isNotNull(adminMember)) {
                continue;
            }

            if (!StrUtil.containsAny(nameCard, "①", "②")) {
                member.setRemark2("备注不规范");
                invalidMember.add(member);
                continue;
            }

            if (ObjectUtil.isNull(chatMember)) {
                member.setRemark2("不在任一聊天群内");
                invalidMember.add(member);
            }
        }
        return invalidMember;
    }
}
