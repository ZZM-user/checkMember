package top.shareus.event;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import top.shareus.command.*;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.util.GroupUtils;
import top.shareus.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 打印命令列表
 *
 * @author 17602
 * @date 2022/8/28 15:06
 */
public class OrderListEvent extends SimpleListenerHost {

    /**
     * 激活指令
     */
    static final String ORDER = "阿秋最帅！";
    /**
     * 命令列表
     */
    static final List<Command> ORDER_LIST = new ArrayList<Command>() {{
        add(GroupCommand.INSTANCE);
        add(InvalidMemberCommand.INSTANCE);
        add(ClearGroupMemberCommand.INSTANCE);
        add(FindInfoByQQNumber.INSTANCE);
        add(UnFinishedQueryCommand.INSTANCE);
    }};

    @EventHandler
    private void onResGroupMessageEvent(GroupMessageEvent event) {
        long id = event.getGroup().getId();

        // 只支持 管理组 和 测试组使用
        if (GroupUtils.hasAnyGroups(id, GroupsConstant.TEST_GROUPS, GroupsConstant.ADMIN_GROUPS)) {
            if (event.getMessage().contentToString().equals(ORDER)) {
                MessageChainBuilder builder = new MessageChainBuilder();
                for (Command command : ORDER_LIST) {
                    builder.add(command.getUsage() + " --- " + command.getDescription() + "\n");
                }
                event.getGroup().sendMessage(builder.asMessageChain());
                LogUtils.info("获取指令集：" + event.getSender().getNick() + "——" + event.getMessage().contentToString());
            }
        }
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
    }
}
