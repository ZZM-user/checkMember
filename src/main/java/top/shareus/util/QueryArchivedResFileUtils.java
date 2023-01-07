package top.shareus.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import top.shareus.common.BotManager;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.core.constant.QiuWenConstant;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.common.mapper.ArchivedFileMapper;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;

/**
 * 查询文件存档 工具类
 *
 * @author zhaojl
 * @date 2023/01/08
 */
public class QueryArchivedResFileUtils {

    /**
     * 检查 求文次数是否正常
     *
     * @param event 事件
     * @return boolean
     */
    public static boolean checkWarring(Long senderId, String nickName) {
        String key = QiuWenConstant.QIU_WEN_REDIS_KEY + senderId;
        int times = QueryArchivedResFileUtils.getTimes(senderId, key);

        if (times > QiuWenConstant.MAX_TIMES_OF_DAY) {
            Bot bot = BotManager.getBot();
            Group group = bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0));
            group.sendMessage("请注意 \n[" + senderId + nickName + "]\n该用户今日已求文第 " + times + " 次");
            return true;
        }

        return false;
    }

    /**
     * 检查模板错误
     *
     * @param senderId   发件人id
     * @param senderName 发送者名字
     */
    public static void checkTemplateError(long senderId, String senderName) {
        String key = QiuWenConstant.ERROR_QIU_WEN_TEMPLATE_REDIS_KEY + senderId;
        int times = QueryArchivedResFileUtils.incrTimes(senderId, key, QiuWenConstant.getExpireTimeOfErrorTemplate());

        if (times >= QiuWenConstant.ERROR_TEMPLATE_MAX_TIMES_OF_WEEK) {
            Bot bot = BotManager.getBot();
            Group group = bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0));
            group.sendMessage("请注意 \n发现[" + senderId + senderName + "]\n该用户本周第 " + times + " 次，求文规范错误");
        }
    }

    /**
     * 是求文
     *
     * @param plainText 纯文本
     * @return boolean
     */
    public static boolean isQiuWen(PlainText plainText) {
        if (ObjectUtil.isNull(plainText)) {
            return false;
        }

        String content = plainText.getContent();
        if (content.length() > 50) {
            LogUtils.info("这哪是求文啊，发公告呢吧…… " + content.length());
            return false;
        }

        if (ReUtil.contains("(求文)|(求)", content)) {
            return true;
        }

        return ReUtil.contains("(书名)", content) && ReUtil.contains("(作者)", content) && ReUtil.contains("(平台)", content);
    }

    /**
     * 提取书信息
     *
     * @param plainText 纯文本
     * @return {@code String}
     */
    public static String extractBookInfo(PlainText plainText) {
        if (ObjectUtil.isNull(plainText)) {
            return "";
        }

        // 匹配 《书名》
        String result = ReUtil.get("[求文](.*)", plainText.getContent(), 0);
        if (StrUtil.isNotEmpty(result)) {
            result = oldRule(result);
        } else {
            // 新规则
            // 书名：静夜思\n作者：李白\n平台：未知
            String[] split = plainText.getContent().split("\n");
            result = split[0].substring(Math.max(split[0].indexOf(":") + 1, split[0].indexOf("：") + 1)).trim();
        }

        // 太长折半
        if (result.length() > 30) {
            result = result.substring(0, result.length() / 2);
        }

        LogUtils.info("求文拆分结果：" + result);

        return result;
    }

    /**
     * 旧规则 待新规则稳定 删除
     *
     * @param result 结果
     * @return {@link String}
     */
    @NotNull
    private static String oldRule(String result) {
        // 移除 书括号 / 求文 之后的内容
        MatchResult matchResultStart = ReUtil.indexOf("(《)|(求文)|(求)", result);
        if (ObjectUtil.isNotNull(matchResultStart)) {
            result = result.substring(matchResultStart.start());
        }

        // 移除 书括号 / 作者 之后的内容
        MatchResult matchResultEnd = ReUtil.indexOf("(》)|(by)|(bY)|(By)|(BY)|(作者)", result);
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
        return result;
    }

    /**
     * 根据书的名字模糊查询
     *
     * @param name 名字
     * @return {@code List<ArchivedFile>}
     */
    public static List<ArchivedFile> findBookInfoByName(String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        }

        List<ArchivedFile> archivedFiles = MybatisPlusUtils.getMapper(ArchivedFileMapper.class).selectBookByName(name);
        if (CollUtil.isEmpty(archivedFiles)) {
            LogUtils.info("查不到相关内容");
            return null;
        }

        LogUtils.info("查询到名为：" + name + " 的书目，共：" + archivedFiles.size() + " 条\n" + Arrays.toString(archivedFiles.toArray()));
        return archivedFiles;
    }

    /**
     * 获取求文次数
     *
     * @param senderId 发件人id
     * @return int
     */
    public static int getTimes(long senderId, String key) {
        try (Jedis jedis = RedisUtils.getJedis()) {
            String times = jedis.get(key);
            return StrUtil.isNotBlank(times) ? Integer.parseInt(times) : 0;
        }
    }

    /**
     * 增加次数
     *
     * @param senderId 发件人id
     */
    public static int incrTimes(long senderId, String key, long expire) {
        try (Jedis jedis = RedisUtils.getJedis()) {
            String oldValue = jedis.get(key);
            if (StrUtil.isNotBlank(oldValue)) {
                jedis.incr(key);
            } else {
                jedis.setex(key, expire, "0");
            }
            return Integer.parseInt(jedis.get(key));
        }
    }

    /**
     * 消减次数
     *
     * @param senderId 发件人id
     */
    public static void decrTimes(long senderId, String key, long expire) {
        try (Jedis jedis = RedisUtils.getJedis()) {
            String oldValue = jedis.get(key);
            if (ObjectUtil.isNotNull(oldValue)) {
                jedis.decr(key);
            } else {
                jedis.setex(key, expire, "0");
            }
        }
    }
}
