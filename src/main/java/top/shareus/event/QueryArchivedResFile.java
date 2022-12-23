package top.shareus.event;

import cn.hutool.core.collection.CollUtil;
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

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;

/**
 * 查询归档res文件
 *
 * @author 17602
 * @date 2022/11/20
 */
public class QueryArchivedResFile extends SimpleListenerHost {

    @EventHandler
    private void onQueryArchivedResFile(GroupMessageEvent event) {
        long id = event.getGroup().getId();
        Long cLong = GroupsConstant.CHAT_GROUPS.stream().filter(r -> r == id).findAny().orElse(null);

        // 不监管 聊天群
        if (ObjectUtil.isNotNull(cLong)) {
            return;
        }

        try {
            PlainText plainText = MessageChainUtils.fetchPlainText(event.getMessage());
            // 不包含 求文 不管
            if (!isQiuWen(plainText)) {
                return;
            }

            if (checkWarring(event)) {
                LogUtils.error(event.getSender().getNameCard() + "/" + event.getSender().getId() + " 求文次数异常！");
                return;
            }

            String bookName = extractBookInfo(plainText);

            List<ArchivedFile> archivedFiles = findBookInfoByName(bookName);

            if (CollUtil.isEmpty(archivedFiles)) {
                LogUtils.info("没查到关于 [" + bookName + "] 的库存信息");
                return;
            }

            // 查到了书目信息 构建消息链
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.add(new At(event.getSender().getId()));
            builder.add("\n小度为你找到了以下内容：");

            archivedFiles.forEach(a ->
                    builder.add("\n名称：" + a.getName() + "\n" + "下载地址：" + ShortUrlUtils.generateShortUrl(a.getArchiveUrl()))
            );

            event.getGroup().sendMessage(builder.build());
        } catch (Exception e) {
            LogUtils.error(e);
        }
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
        if (ObjectUtil.isNull(plainText)) {
            return false;
        }

        String content = plainText.getContent();
        if (content.length() > 50) {
            LogUtils.info("这哪是求文啊，发公告呢吧…… " + content.length());
            return false;
        }

        return ReUtil.contains("(求文)|(求)", content);
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

        // 匹配 《书名》
        String result = ReUtil.get("[求文](.*)", plainText.getContent(), 0);

        // 移除 书括号 / 求文 之后的内容
        MatchResult matchResultStart = ReUtil.indexOf("(《)|(求文)|(求)", result);
        if (ObjectUtil.isNotNull(matchResultStart)) {
            result = result.substring(matchResultStart.start());
        }

        // 移除 书括号 / 作者 之后的内容
        MatchResult matchResultEnd = ReUtil.indexOf("(》)|(by)|(作者)", result);
        if (ObjectUtil.isNotNull(matchResultEnd)) {
            result = result.substring(0, matchResultEnd.start());
        }

        // 最后的替换
        result = result.replaceFirst("(求文)|(求)", "")
                .replace(":", "")
                .replace("：", "")
                .replace("《", "")
                .replace("\n", "")
                .trim();

        // 太长折半
        if (result.length() > 30) {
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
        if (StrUtil.isBlank(name)) {
            return null;
        }

        QueryWrapper<ArchivedFile> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        wrapper.orderByAsc("archive_date");
        wrapper.last("limit 10");
        List<ArchivedFile> archivedFiles = null;

        try (SqlSession session = MybatisPlusUtils.sqlSessionFactory.openSession(true)) {
            ArchivedFileMapper mapper = session.getMapper(ArchivedFileMapper.class);
            archivedFiles = mapper.selectList(wrapper);
        } catch (Exception e) {
            LogUtils.error(e);
        }
        if (CollUtil.isEmpty(archivedFiles)) {
            LogUtils.info("查不到相关内容");
            return null;
        }

        LogUtils.info("查询到名为：" + name + " 的书目，共：" + archivedFiles.size() + " 条\n" + Arrays.toString(archivedFiles.toArray()));
        return archivedFiles;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
    }
}
