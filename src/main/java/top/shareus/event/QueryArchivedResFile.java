package top.shareus.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import top.shareus.common.BotManager;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.core.constant.QiuWenConstant;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.common.mapper.ArchivedFileMapper;
import top.shareus.util.*;

import java.util.List;

/**
 * 查询归档res文件
 *
 * @author 17602
 * @date 2022/11/20
 */
public class QueryArchivedResFile extends SimpleListenerHost {

    /**
     * 存档文件映射器
     */
    private static ArchivedFileMapper archivedFileMapper = MybatisPlusUtils.getMapper(ArchivedFileMapper.class);


    @EventHandler
    private void onQueryArchivedResFile(GroupMessageEvent event) {
        long id = event.getGroup().getId();
        Long cLong = GroupsConstant.CHAT_GROUPS.stream().filter(r -> r == id).findAny().orElse(null);

        // 不监管 聊天群
        if (ObjectUtil.isNotNull(cLong)) {
            return;
        }

        MessageChain message = event.getMessage();
        PlainText plainText = MessageChainUtils.fetchPlainText(message);
        // 不包含 求文 不管
        if (ObjectUtil.isNull(plainText) || !isQiuWen(plainText)) {
            return;
        }

        boolean warring = checkWarring(event);
        if (warring) {
            LogUtils.error(event.getSender().getNameCard() + "/" + event.getSender().getId() + " 求文次数异常！");
            return;
        }
        String bookName = extractBookInfo(plainText);
        List<ArchivedFile> archivedFiles = findBookInfoByName(bookName);

        if (ObjectUtil.isNull(archivedFiles) || archivedFiles.size() <= 0) {
            LogUtils.info("没查到关于：" + bookName + " 的库存信息");
            return;
        }

        // 查到了书目信息 构建消息链
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.add(new At(event.getSender().getId()));
        builder.add("\n小度为你查到了以下内容：");

        archivedFiles.forEach(a -> {
            builder.add("\n名称：" + a.getName() + "\n" + "下载地址：" + ShortUrlUtils.generateShortUrl(a.getArchiveUrl()));
        });
        event.getGroup().sendMessage(builder.build());
    }

    /**
     * 检查 求文次数是否正常
     *
     * @param event 事件
     * @return boolean
     */
    private boolean checkWarring(GroupMessageEvent event) {
        String key = QiuWenConstant.QIU_WEN_REDIS_KEY + event.getSender().getId();
        Jedis jedis = RedisUtils.getJedis();

        String oldValue = jedis.get(key);
        if (ObjectUtil.isNotNull(oldValue)) {
            jedis.incr(key);

            if (Long.parseLong(oldValue) >= QiuWenConstant.MAX_TIMES_OF_DAY) {
                Bot bot = BotManager.getBot();
                Group group = bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0));
//                Group group = bot.getGroup(GroupsConstant.TEST_GROUPS.get(0));
                group.sendMessage("请注意 【" + event.getSender().getId() + event.getSender().getNameCard() + "】该用户今日已求文 " + oldValue + " 次");
                return true;
            }
        } else {
            jedis.setex(key, QiuWenConstant.getExpireTime(), "1");
        }
        return false;
    }

    /**
     * 是求文
     *
     * @param plainText 纯文本
     * @return boolean
     */
    private boolean isQiuWen(PlainText plainText) {
        String content = plainText.getContent();
        if (content.length() > 50) {
            LogUtils.info("这哪是求文啊，发公告呢吧……" + content.length());
        }

        return ReUtil.contains("求文", content);
    }

    /**
     * 提取书信息
     *
     * @param plainText 纯文本
     * @return {@code String}
     */
    private String extractBookInfo(PlainText plainText) {
        if (ObjectUtil.isNull(plainText)) {
            return "";
        }

        String content = plainText.getContent();

        // 匹配 《书名》
        String result = ReUtil.get("《(.*)》", content, 0)
                .replace("《", "")
                .replace("》", "");
        // 太长折半
        if (result.length() > 10) {
            result = result.substring(0, result.length() / 2);
        }

        LogUtils.info("求文拆分结果：" + result);

        return result;
    }

    /**
     * 根据书的名字模糊查询
     *
     * @param name 名字
     * @return {@code List<ArchivedFile>}
     */
    private List<ArchivedFile> findBookInfoByName(String name) {
        QueryWrapper<ArchivedFile> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);
        wrapper.last("limit 5");
        List<ArchivedFile> archivedFiles = null;

        try (SqlSession session = MybatisPlusUtils.sqlSessionFactory.openSession(true)) {
            ArchivedFileMapper mapper = session.getMapper(ArchivedFileMapper.class);
            archivedFiles = mapper.selectList(wrapper);
        } catch (Exception e) {
            LogUtils.error(e);
        }
        if (archivedFiles.size() <= 0) {
            return null;
        }

        LogUtils.info("查询到名为：" + name + " 的书目，共：" + archivedFiles.size() + " 条\n" + archivedFiles.toArray().toString());
        return archivedFiles;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
    }
}
