package top.shareus.event;

import cn.hutool.core.util.ObjectUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import org.jetbrains.annotations.NotNull;
import top.shareus.common.core.constant.BanResWordConstant;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.util.LogUtils;

/**
 * 监听聊天事件 撤销违禁消息
 *
 * @author 17602
 * @date 2022/8/28 9:55
 */
public class ResChatEvent extends SimpleListenerHost {
    
    @EventHandler
    private void onAdminGroupMessageEvent(GroupMessageEvent event) {
        long id = event.getGroup().getId();
        Long rLong = GroupsConstant.RES_GROUPS.stream().filter(r -> r == id).findAny().orElse(null);
        
        if (ObjectUtil.isNotNull(rLong)) {
            if (BanResWordConstant.hasBanWord(event.getMessage().contentToString())) {
                // 禁它言
                event.getSender().mute(BanResWordConstant.MUTE_SECONDS);
                // 撤它消息
                MessageSource.recall(event.getMessage());
                String message = "尝试撤回消息 " + event.getSender().getNick() + "：" + event.getMessage().contentToString();
                LogUtils.info(message);
                // 通知群
                event.getBot().getGroup(GroupsConstant.ADMIN_GROUPS.get(0)).sendMessage(message);
            }
        }
    }
    
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getCause().getMessage());
    }
}
