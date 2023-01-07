package top.shareus.job.querylog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.task.Task;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import top.shareus.common.BotManager;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.domain.QueryLog;
import top.shareus.common.mapper.QueryLogMapper;
import top.shareus.util.LogUtils;
import top.shareus.util.MybatisPlusUtils;

import java.util.List;

/**
 * 反馈 每天没有关闭的求文记录
 *
 * @author zhaojl
 * @date 2023/01/08
 */
public class Feedback implements Task {
    /**
     * 发送通知
     *
     * @param queryLogs 查询日志
     */
    public static void senderNotice(List<QueryLog> queryLogs) {
        Bot bot = BotManager.getBot();
        Group group = bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0));
        queryLogs.forEach(queryLog -> {
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.add("今日未完成的求文：");
            builder.add("\n求文内容: " + queryLog.getContent());
            builder.add("\n求文QQ: " + queryLog.getSenderId());
            builder.add("\n求文名称: " + queryLog.getSenderName());
            builder.add("\n求文时间: " + DateUtil.formatDateTime(queryLog.getSendTime()));
            group.sendMessage(builder.build());
        });
    }

    /**
     * 查询几天的
     *
     * @param day 天数
     * @return {@link List}<{@link QueryLog}>
     */
    public static List<QueryLog> queryHowDay(Integer day) {
        return MybatisPlusUtils.getMapper(QueryLogMapper.class).selectUnfinishedQuery(day);
    }

    @Override
    public void execute() {
        List<QueryLog> queryLogs = queryHowDay(0);
        if (CollUtil.isEmpty(queryLogs)) {
            LogUtils.info("今日求文已完成！");
            return;
        }

        senderNotice(queryLogs);
    }
}
