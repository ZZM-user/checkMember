package top.shareus.event;

import cn.hutool.core.util.ObjectUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;
import top.shareus.CheckMember;
import top.shareus.common.constant.GroupsConstant;

/**
 * @Author： 17602
 * @Date： 2022/8/28 16:27
 * @Desc： 转发管理群组信息
 **/
public class ForwardMessage extends SimpleListenerHost {
    @EventHandler
    private void onResGroupMessageEvent(GroupMessageEvent event) {
        long id = event.getGroup().getId();
        Long aLong = GroupsConstant.ADMIN_GROUPS.stream().filter(r -> r == id).findAny().orElse(null);
        
        if (ObjectUtil.isNotNull(aLong)) {
            Bot bot = event.getBot();
            Group group = bot.getGroup(GroupsConstant.TEST_GROUPS.get(0));
            String message = event.getMessage().contentToString();
            String senderInfo = event.getSenderName();
            group.sendMessage(senderInfo + "：" + message);
        }
    }
    
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        CheckMember.INSTANCE.getLogger().error(context + "\n" + exception.getCause().getMessage());
    }
}
