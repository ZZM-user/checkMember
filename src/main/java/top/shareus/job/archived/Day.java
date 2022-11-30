package top.shareus.job.archived;

import cn.hutool.core.lang.Assert;
import cn.hutool.cron.task.Task;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.apache.ibatis.session.SqlSession;
import top.shareus.common.BotManager;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.mapper.ArchivedFileMapper;
import top.shareus.util.LogUtils;
import top.shareus.util.MybatisPlusUtils;

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
        Integer hasArchived = null;
        try (SqlSession session = MybatisPlusUtils.sqlSessionFactory.openSession(true)) {
            ArchivedFileMapper mapper = session.getMapper(ArchivedFileMapper.class);
            hasArchived = mapper.countByYesterday();
        }
        Assert.notNull(hasArchived, "获取昨日归档信息失败!");

        MessageChainBuilder builder = new MessageChainBuilder();
        builder.add("昨日资源群归档文件数量：" + hasArchived);
        if (hasArchived < 10) {
        }
        Bot bot = BotManager.getBot();
        // 小于 10 估计出问题了
        if (hasArchived < 10) {
            builder.add(new At(2657272578L));
            Group group = bot.getGroupOrFail(GroupsConstant.TEST_GROUPS.get(0));
            group.sendMessage(builder.build());
        }
        Group group = bot.getGroupOrFail(GroupsConstant.ADMIN_GROUPS.get(0));
//        Group group = bot.getGroupOrFail(GroupsConstant.TEST_GROUPS.get(0));
        LogUtils.info(builder.build().toString());
        group.sendMessage(builder.build());
    }
}
