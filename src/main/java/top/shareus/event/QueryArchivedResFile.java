package top.shareus.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.core.constant.QiuWenConstant;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.util.*;

import java.util.List;

/**
 * 查询归档res文件
 *
 * @author 17602
 * @date 2022/11/20
 */
public class QueryArchivedResFile extends SimpleListenerHost {

    @EventHandler
    private void onQueryArchivedResFile(GroupMessageEvent event) {
        long senderId = event.getSender().getId();
        long groupId = event.getGroup().getId();

        // 不监管 聊天群、管理群
        if (GroupUtils.hasAnyGroups(groupId, GroupsConstant.CHAT_GROUPS, GroupsConstant.ADMIN_GROUPS)) {
            return;
        }

        try {
            PlainText plainText = MessageChainUtils.fetchPlainText(event.getMessage());
            // 不包含 求文 不管
            if (!QueryArchivedResFileUtils.isQiuWen(plainText)) {
                return;
            }

            if (QueryArchivedResFileUtils.checkWarring(senderId, event.getSenderName())) {
                LogUtils.error(event.getSender().getNameCard() + "/" + senderId + " 求文次数异常！");
                return;
            }

            // 提取书名
            String bookName = QueryArchivedResFileUtils.extractBookInfo(plainText);

            // 规范错误
            if (StrUtil.isEmpty(bookName)) {
                LogUtils.info("求文规范错误！");
                QueryArchivedResFileUtils.checkTemplateError(senderId, event.getSenderName());
                return;
            }

            // 查询
            List<ArchivedFile> archivedFiles = QueryArchivedResFileUtils.findBookInfoByName(bookName);

            // 求文记录 
            QueryLogUtils.recordLog(event, plainText.getContent(), bookName, archivedFiles);

            if (CollUtil.isEmpty(archivedFiles)) {
                LogUtils.info("没查到关于 [" + bookName + "] 的库存信息");
                return;
            }

            // 求文次数 + 1
            String key = QiuWenConstant.QIU_WEN_REDIS_KEY + senderId;
            QueryArchivedResFileUtils.incrTimes(senderId, key, QiuWenConstant.getExpireTime());

            // 查到了书目信息 构建消息链
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.add(new At(senderId));
            builder.add("\n小度为你找到了以下内容：");

            archivedFiles.forEach(a ->
                    builder.add("\n名称：" + a.getName() + "\n" + "下载地址：" + ShortUrlUtils.generateShortUrl(a.getArchiveUrl()))
            );

            event.getGroup().sendMessage(builder.build());
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
    }
}
