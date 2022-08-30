package top.shareus.event;

import cn.hutool.core.util.ObjectUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import org.jetbrains.annotations.NotNull;
import top.shareus.CheckMember;
import top.shareus.common.constant.BanResWordConstant;
import top.shareus.common.constant.GroupsConstant;

/**
 * @Author： 17602
 * @Date： 2022/8/28 9:55
 * @Desc： 监听聊天事件
 **/
public class ResChatEvent extends SimpleListenerHost {
    
    @EventHandler
    private void onResGroupMessageEvent(GroupMessageEvent event) {
        long id = event.getGroup().getId();
        Long aLong = GroupsConstant.RES_GROUPS.stream().filter(r -> r == id).findAny().orElse(null);
        // Long aLong = GroupsConstant.TEST_GROUPS.stream().filter(r -> r == id).findAny().orElse(null);
        
        if (ObjectUtil.isNotNull(aLong)) {
            if (BanResWordConstant.hasBanWord(event.getMessage().contentToString())) {
                // 禁它言
                event.getSender().mute(BanResWordConstant.MUTE_SECONDS);
                // 撤它消息
                MessageSource.recall(event.getMessage());
                String message = "尝试撤回消息 " + event.getSender().getNick() + "：" + event.getMessage().contentToString();
                CheckMember.INSTANCE.getLogger().info(message);
                // 通知群
                event.getBot().getGroup(GroupsConstant.ADMIN_GROUPS.get(0)).sendMessage(message);
            }
        }
    }
    
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        CheckMember.INSTANCE.getLogger().error(context + "\n" + exception.getCause().getMessage());
    }
}
