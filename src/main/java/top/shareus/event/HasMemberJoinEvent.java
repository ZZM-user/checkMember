package top.shareus.event;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.util.GroupUtils;
import top.shareus.util.ImageUtils;
import top.shareus.util.LogUtils;

/**
 * 新成员加入事件
 *
 * @author 17602
 * @date 2022/8/28 15:51
 */
public class HasMemberJoinEvent extends SimpleListenerHost {
    @EventHandler
    private void onResGroupMessageEvent(MemberJoinEvent event) {
        long id = event.getGroupId();

        if (GroupUtils.hasAnyGroups(id, GroupsConstant.ADMIN_GROUPS, GroupsConstant.CHAT_GROUPS)) {
            NormalMember member = event.getMember();
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.add(new At(member.getId()));
            builder.add(" 欢迎欢迎！");
            // 构建头像 发送
            Image image = ImageUtils.create(event.getGroup(), member.getAvatarUrl());
            builder.add(image);
            event.getGroup().sendMessage(builder.build());
        }
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
    }
}
