package top.shareus.command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.NotNull;
import top.shareus.CheckMember;
import top.shareus.common.domain.QueryLog;
import top.shareus.job.querylog.Feedback;

import java.util.List;

/**
 * 今天未完成查询 命令
 *
 * @author zhaojl
 * @date 2023/01/08
 */
public class UnFinishedQueryCommand extends JRawCommand {

    public static final UnFinishedQueryCommand INSTANCE = new UnFinishedQueryCommand();

    private UnFinishedQueryCommand() {
        // 使用插件主类对象作为指令拥有者；设置主指令名为 "test"
        super(CheckMember.INSTANCE, "今天未完成的求文");
        // 可选设置如下属性
        // setPermission();
        // 设置用法，这将会在 /help 中展示
        setUsage("/unFinishedQiuWen [day]");
        // 设置描述，也会在 /help 中展示
        setDescription("获取今天还未完成的求文任务");
        // 设置指令前缀是可选的，即使用 `test` 也能执行指令而不需要 `/test`
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        Integer defaultDay = 0;

        if (args.size() > 0) {
            SingleMessage singleMessage = args.get(0);
            String string = singleMessage.contentToString();
            defaultDay = Integer.valueOf(string);
        }

        List<QueryLog> queryLogs = Feedback.queryHowDay(defaultDay);
        Feedback.senderNotice(queryLogs);
    }
}
