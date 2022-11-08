package top.shareus.event;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import top.shareus.common.constant.GroupsConstant;
import top.shareus.util.LogUtils;
import top.shareus.util.MessageChainUtils;

/**
 * 转发好友的闪照信息
 *
 * @author 17602
 * @date 2022/11/8:14:26
 */
public class ForwardFriendFlashImageMessage extends SimpleListenerHost {
    
    @EventHandler
    private void onFriendFlashImageMessageEvent(FriendMessageEvent event) {
        // 监听 【所有】 闪照
        MessageChain message = event.getMessage();
        // 获取闪照
        FlashImage flashImage = MessageChainUtils.fetchFlashImage(message);
        
        if (ObjectUtil.isNotNull(flashImage)) {
            // 获取测试组
            Bot bot = event.getBot();
            Group group = bot.getGroup(GroupsConstant.TEST_GROUPS.get(0));
            
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.add("【截取的闪照】");
            // 发送者
            builder.add(event.getSenderName());
            // 发送时间
            builder.add(DateTime.of(event.getTime()).toDateStr());
            // 闪照
            builder.add(flashImage.getImage());
            
            group.sendMessage(builder.build());
        }
    }
    
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getCause().getMessage());
    }
}
