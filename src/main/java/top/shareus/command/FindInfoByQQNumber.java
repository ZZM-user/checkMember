package top.shareus.command;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.shareus.CheckMember;
import top.shareus.common.BotManager;
import top.shareus.common.NormalMemberVO;
import top.shareus.util.GroupUtils;
import top.shareus.util.LogUtils;
import top.shareus.util.NormalMemberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通过QQ号码查询信息
 *
 * @author 17602
 * @date 2022/10/29 17:03
 **/
public final class FindInfoByQQNumber extends JRawCommand {
    public static final FindInfoByQQNumber INSTANCE = new FindInfoByQQNumber();

    public FindInfoByQQNumber() {
        // 使用插件主类对象作为指令拥有者；设置主指令名为 "test"
        super(CheckMember.INSTANCE, "find");
        // 可选设置如下属性

        // 设置用法，这将会在 /help 中展示
        setUsage("/find <[QQ]|[NickName]|[Nick]|[SpecialTitle]>");
        // 设置描述，也会在 /help 中展示
        setDescription("获取指定QQ号或指定昵称的群成员信息");
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
        try {
            String keyword = args.get(0).contentToString();
            if (StrUtil.isBlank(keyword)) {
                sender.sendMessage("参数不能为空哦！");
                return;
            }
            List<NormalMemberVO> memberList = findMembers(keyword);
            if (memberList.size() > 0) {
                StringBuilder builder = new StringBuilder();

                builder.append("找到了" + memberList.size() + "条信息：\n");
                // 格式化出成员信息
                memberList.forEach(m -> builder.append(NormalMemberUtils.format(m, true)));
                sender.sendMessage(builder.toString());
                LogUtils.info("\t请求到了 " + memberList.size() + " 条 关于 【" + keyword + "】 的信息");
            } else {
                sender.sendMessage("查不到关于【" + keyword + "】的信息");
            }
        } catch (Exception e) {
            LogUtils.error(e);
            sender.sendMessage("操作失败，请联系管理员！");
        }
    }

    /**
     * 查询成员
     *
     * @param keyword 关键字
     * @return {@link List}<{@link NormalMember}>
     */
    private List<NormalMemberVO> findMembers(String keyword) {
        Bot bot = BotManager.getBot();
        // 率先读取出所有群成员信息
        Map<String, List<NormalMemberVO>> allGroupMembers = GroupUtils.getAllGroupMembers(bot);
        List<NormalMemberVO> adminMemberList = allGroupMembers.get("admin");
        List<NormalMemberVO> resMemberList = allGroupMembers.get("res");
        List<NormalMemberVO> chatMemberList = allGroupMembers.get("chat");

        // 收集相匹配的数据
        List<NormalMemberVO> collect = new ArrayList<>();
        collect.addAll(adminMemberList.stream().filter(m -> equal(m, keyword)).collect(Collectors.toList()));
        collect.addAll(resMemberList.stream().filter(m -> equal(m, keyword)).collect(Collectors.toList()));
        collect.addAll(chatMemberList.stream().filter(m -> equal(m, keyword)).collect(Collectors.toList()));

        return collect;
    }


    /**
     * 是否符合条件
     *
     * @param member  成员
     * @param keyword 关键字
     * @return boolean
     */
    private boolean equal(NormalMemberVO member, String keyword) {
        // 关键字 同 QQ号、昵称、备注、头衔 匹配
        if (NumberUtil.isLong(keyword)) {
            return NumberUtil.parseLong(keyword) == member.getId();
        }
        return StrUtil.contains(member.getNameCard(), keyword) || StrUtil.contains(member.getNick(), keyword) || StrUtil.contains(member.getSpecialTitle(), keyword);
    }
}
