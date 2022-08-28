package top.shareus.event;

import cn.hutool.core.util.ObjectUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import top.shareus.CheckMember;
import top.shareus.command.ClearGroupMemberCommand;
import top.shareus.command.GroupCommand;
import top.shareus.command.InvalidMemberCommand;
import top.shareus.common.constant.GroupsConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： 17602
 * @Date： 2022/8/28 15:06
 * @Desc： 打印命令列表
 **/
public class OrderListEvent extends SimpleListenerHost {
    static final String ORDER = "阿秋最帅！";
    static final List<Command> ORDER_LIST = new ArrayList<Command>() {{
        add(GroupCommand.INSTANCE);
        add(InvalidMemberCommand.INSTANCE);
        add(ClearGroupMemberCommand.INSTANCE);
    }};
    
    @EventHandler
    private void onResGroupMessageEvent(GroupMessageEvent event) {
        long id = event.getGroup().getId();
        Long aLong = GroupsConstant.ADMIN_GROUPS.stream().filter(r -> r == id).findAny().orElse(null);
        
        if (ObjectUtil.isNotNull(aLong)) {
            if (event.getMessage().contentToString().equals(ORDER)) {
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(new At(event.getSender().getId()));
                for (Command command : ORDER_LIST) {
                    builder.add(command.getUsage() + "\t" + command.getDescription());
                }
                CheckMember.INSTANCE.getLogger().info("撤回消息：" + event.getSender().getNick() + "——" + event.getMessage().contentToString());
            }
        }
    }
    
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        CheckMember.INSTANCE.getLogger().error(context + "\n" + exception.getCause().getMessage());
    }
}
