package top.shareus.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.file.AbsoluteFile;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.MessageChain;
import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.common.mapper.ArchivedFileMapper;
import top.shareus.util.*;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 转存资源群文件、并做记录
 *
 * @author 17602
 * @date 2022/11/19
 */
public class ArchivedResFile extends SimpleListenerHost {

    /**
     * 文件下载路径 for Linux
     */
    public static final String FILE_DOWNLOAD_PATH = "/opt/download/groupFile/";

    @EventHandler
    private void onArchivedResFileEvent(GroupMessageEvent event) {
        long id = event.getGroup().getId();
        // 只监管 资源群
        if (GroupUtils.notHasAnyGroups(id, GroupsConstant.RES_GROUPS, GroupsConstant.TEST_GROUPS)) {
            return;
        }

        // 监听 【所有】 文件
        MessageChain message = event.getMessage();
        // 获取文件
        FileMessage fileMessage = MessageChainUtils.fetchFileMessage(message);

        if (ObjectUtil.isNotNull(fileMessage)) {
            // 下载文件
            ArchivedFile archivedFile = downloadFile(event, fileMessage);

            if (ObjectUtil.isNotNull(archivedFile)) {
                // 转存文件，存放盘，获取连接
                LogUtils.info("文件下载成功：" + archivedFile.getName());
                File file = new File(archivedFile.getArchiveUrl());

                LogUtils.info("归档路径：" + archivedFile.getArchiveUrl());
                String uploadFilePath = AlistUtils.uploadFile(file);
                if (StrUtil.isNotBlank(uploadFilePath)) {
                    LogUtils.info(archivedFile.toString());
                    // 将信息 写入数据库
                    try (SqlSession session = MybatisPlusUtils.sqlSessionFactory.openSession(true)) {
                        ArchivedFileMapper mapper = session.getMapper(ArchivedFileMapper.class);
                        mapper.insert(archivedFile);
                    } catch (Exception e) {
                        LogUtils.error(e);
                    }
                }
                LogUtils.info(archivedFile.getName() + " 存档完成！");
            }
        }
    }

    /**
     * 下载文件
     *
     * @param event       事件
     * @param fileMessage 文件信息
     * @return {@code ArchivedFile}
     */
    private static ArchivedFile downloadFile(GroupMessageEvent event, FileMessage fileMessage) {
        AbsoluteFile file = fileMessage.toAbsoluteFile(event.getGroup());
        // 构造 文件归档url
        String archiveUrl = FILE_DOWNLOAD_PATH + fileMessage.getName();
        // 下载文件
        long len = HttpUtil.downloadFile(file.getUrl(), new File(archiveUrl));

        LogUtils.info(fileMessage.getName() + ": len = " + len);

        if (len > 0) {
            // 碰撞先关
            String md5 = String.valueOf(ByteUtil.bytesToLong(file.getMd5()));
            List<ArchivedFile> archivedFiles = null;

            try (SqlSession session = MybatisPlusUtils.sqlSessionFactory.openSession(true)) {
                ArchivedFileMapper mapper = session.getMapper(ArchivedFileMapper.class);
                archivedFiles = mapper.selectRepeatFileByMd5(md5);
            } catch (Exception e) {
                LogUtils.error(e);
            }

            return ArchivedFile.builder()
                    .id(IdUtil.simpleUUID())
                    .name(file.getName())
                    .size(file.getSize())
                    .md5(md5)
                    .archiveDate(new Date())
                    .archiveUrl(archiveUrl)
                    .originUrl(file.getUrl())
                    .senderId(event.getSender().getId())
                    .enabled(CollUtil.isEmpty(archivedFiles) ? 0 : 1)
                    .build();
        }
        return null;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        LogUtils.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
    }
}
