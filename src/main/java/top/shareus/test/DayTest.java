package top.shareus.test;

import org.junit.jupiter.api.Test;
import top.shareus.common.domain.QueryLog;
import top.shareus.common.mapper.QueryLogMapper;
import top.shareus.util.MybatisPlusUtils;

import java.util.List;

class DayTest {

    @Test
    void execute() {
        QueryLogMapper queryLogMapper = MybatisPlusUtils.getMapper(QueryLogMapper.class);
        Integer queryLogs = queryLogMapper.countByYesterday();
        List<QueryLog> unfinishedQuery = queryLogMapper.selectUnfinishedQuery(1);

        System.out.println("queryLogs = " + queryLogs);
        System.out.println("unfinishedQuery = " + unfinishedQuery.size());
    }
}