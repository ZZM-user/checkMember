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
import top.shareus.common.domain.ShareFileStar;
import top.shareus.common.mapper.ArchivedFileMapper;
import top.shareus.util.LogUtils;
import top.shareus.util.MybatisPlusUtils;

import java.text.NumberFormat;
import java.util.List;

/**
 * 周
 *
 * @author zhaojl
 * @date 2022/11/27
 */
public class Week implements Task {
    @Override
    public void execute() {
        // 每周发送统计信息
        Integer hasArchived = null;
        List<ShareFileStar> stars = null;
        try (SqlSession session = MybatisPlusUtils.sqlSessionFactory.openSession(true)) {
            ArchivedFileMapper mapper = session.getMapper(ArchivedFileMapper.class);
            hasArchived = mapper.countByDaysOfBefore(7);
            stars = mapper.computedFileStar(7);
        }
        Assert.notNull(hasArchived, "获取本周归档信息失败!");
        Assert.notNull(stars, "获取本周分享之星失败!");

        ShareFileStar star = stars.get(0);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("本周总结：");
        builder.add("\n本周资源群归档文件数量：" + hasArchived);
        builder.add("\n本周分享之星：");
        builder.add(new At(star.getSenderId()));
        builder.add("\nTA本周为我们分享了 " + star.getTimes() + " 次文件");
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        builder.add("\n占本周的 " + numberFormat.format((float) star.getTimes() / (float) hasArchived * 100) + "%");
        builder.add("\n大家继续努力！");

        Bot bot = BotManager.getBot();
//        Group group = bot.getGroupOrFail(GroupsConstant.ADMIN_GROUPS.get(0));
        Group group = bot.getGroupOrFail(GroupsConstant.TEST_GROUPS.get(0));
        LogUtils.info(builder.build().toString());
        group.sendMessage(builder.build());

        bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0)).sendMessage(builder.build());
    }
}
