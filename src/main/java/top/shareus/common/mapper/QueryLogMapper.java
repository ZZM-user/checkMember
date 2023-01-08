package top.shareus.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.shareus.common.domain.QueryLog;

import java.util.List;

/**
 * 查询日志映射器
 *
 * @author zhaojl
 * @date 2023/01/07
 */
public interface QueryLogMapper extends BaseMapper<QueryLog> {

    /**
     * 未完成的查询
     *
     * @return {@link List}<{@link QueryLog}>
     */
    @Select("SELECT * FROM query_log WHERE `status` = 1")
    List<QueryLog> selectUnfinishedQuery();

    /**
     * 查询日志 书名
     *
     * @param bookName 书名
     * @return {@link List}<{@link QueryLog}>
     */
    @Select("SELECT * FROM query_log WHERE `status` = 1 and  #{bookName} LIKE concat('%',`extract`,'%')")
    List<QueryLog> queryLogByBookName(String bookName);

    /**
     * 选择未完成查询
     *
     * @param day 一天
     * @return {@link List}<{@link QueryLog}>
     */
    @Select("SELECT * FROM query_log WHERE `status` = 1 and DATEDIFF(NOW(),send_time) = #{day}")
    List<QueryLog> selectUnfinishedQuery(Integer day);

    /**
     * 昨天 求问数量
     *
     * @return {@link List}<{@link QueryLog}>
     */
    @Select("SELECT count(1) FROM query_log WHERE DATEDIFF(NOW(), send_time) = 1")
    List<QueryLog> countByYesterday();

}
