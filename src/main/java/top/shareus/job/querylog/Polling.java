package top.shareus.job.querylog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import top.shareus.common.core.constant.QiuWenConstant;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.common.domain.QueryLog;
import top.shareus.common.mapper.QueryLogMapper;
import top.shareus.util.LogUtils;
import top.shareus.util.MybatisPlusUtils;
import top.shareus.util.QueryArchivedResFileUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 轮询 检查求文是否已完成
 *
 * @author zhaojl
 * @date 2023/01/07
 */
public class Polling implements Task {
    /**
     * 完成查询
     *
     * @param queryLog       查询日志
     * @param bookInfoByName 图书信息名字
     */
    private static void finishQuery(QueryLog queryLog, List<ArchivedFile> archivedFiles) {
        if (CollUtil.isNotEmpty(archivedFiles)) {
            // 按归档时间排正序
            if (archivedFiles.size() > 1) {
                archivedFiles.stream().sorted(Comparator.comparing(ArchivedFile::getArchiveDate));
            }

            ArchivedFile archivedFile = archivedFiles.get(0);
            queryLog.setStatus(0);
            queryLog.setAnswerId(archivedFile.getSenderId());
            queryLog.setResult(JSONUtil.toJsonPrettyStr(archivedFile));
            queryLog.setFinishTime(archivedFile.getArchiveDate());
            MybatisPlusUtils.getMapper(QueryLogMapper.class).updateById(queryLog);
            String key = QiuWenConstant.QIU_WEN_REDIS_KEY + queryLog.getSenderId();
            QueryArchivedResFileUtils.incrTimes(queryLog.getSenderId(), key, QiuWenConstant.getExpireTime());
            LogUtils.info("该求文任务完成: " + queryLog);
        }
    }

    /**
     * 完成查询
     *
     * @param queryLog     查询日志
     * @param archivedFile 存档文件
     */
    public static void finishQuery(QueryLog queryLog, ArchivedFile archivedFile) {
        if (ObjectUtil.isNotNull(archivedFile)) {
            Polling.finishQuery(queryLog, new ArrayList() {{
                add(archivedFile);
            }});
        }
    }

    @Override
    public void execute() {
        LogUtils.info("开始对求文任务检查……");

        List<QueryLog> queryLogs = MybatisPlusUtils.getMapper(QueryLogMapper.class).selectUnfinishedQuery();

        LogUtils.info("待反馈求文任务数量：" + queryLogs.size());
        if (CollUtil.isEmpty(queryLogs)) {
            return;
        }

        // 拿拆出来的书名 回查 有的话按早发的人来 并且更新log 增加求文次数
        queryLogs.forEach(queryLog -> {
            String extract = queryLog.getExtract();
            if (StrUtil.isNotBlank(extract)) {
                List<ArchivedFile> bookInfoByName = QueryArchivedResFileUtils.findBookInfoByName(extract);
                finishQuery(queryLog, bookInfoByName);
            }
        });

        LogUtils.info("求文任务检查已结束");
    }
}
