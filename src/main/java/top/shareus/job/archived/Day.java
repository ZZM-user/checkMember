package top.shareus.job.archived;

import cn.hutool.cron.task.Task;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import top.shareus.common.BotManager;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.domain.QueryLog;
import top.shareus.common.mapper.ArchivedFileMapper;
import top.shareus.common.mapper.QueryLogMapper;
import top.shareus.util.LogUtils;
import top.shareus.util.MybatisPlusUtils;

import java.util.List;

/**
 * 一天
 *
 * @author zhaojl
 * @date 2022/11/27
 */
public class Day implements Task {

    @Override
    public void execute() {
        // 每天发送统计信息
        Integer hasArchived = MybatisPlusUtils.getMapper(ArchivedFileMapper.class).countByYesterday();

        QueryLogMapper queryLogMapper = MybatisPlusUtils.getMapper(QueryLogMapper.class);
        List<QueryLog> queryLogs = queryLogMapper.countByYesterday();
        List<QueryLog> unfinishedQuery = queryLogMapper.selectUnfinishedQuery(1);

        MessageChainBuilder builder = new MessageChainBuilder();
        builder.add("昨日资源群归档文件数量：" + hasArchived);
        builder.add("\n昨日资源群求文数量：" + queryLogs.size());
        builder.add("\n昨日资源群未完成的求文：" + unfinishedQuery.size());

        Bot bot = BotManager.getBot();
        Group group = bot.getGroupOrFail(GroupsConstant.TEST_GROUPS.get(0));
        LogUtils.info(builder.build().toString());
        group.sendMessage(builder.build());

        bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0)).sendMessage(builder.build());
    }
}
