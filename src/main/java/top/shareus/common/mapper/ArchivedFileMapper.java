package top.shareus.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.common.domain.ShareFileStar;

import java.util.List;

/**
 * 存档文件 Mapper
 *
 * @author 17602
 * @date 2022/11/19
 */
public interface ArchivedFileMapper extends BaseMapper<ArchivedFile> {

    /**
     * 今天数量
     *
     * @return {@link Integer}
     */
    @Select("SELECT count(1) FROM archived_file WHERE DATEDIFF(NOW(),archive_date) = 1")
    Integer countByYesterday();

    /**
     * n 之前日子
     *
     * @param day 一天
     * @return {@link Integer}
     */
    @Select("SELECT count(1) FROM archived_file WHERE  DATEDIFF(NOW(),archive_date) < #{day}")
    Integer countByDaysOfBefore(int day);

    /**
     * 计算文件明星
     *
     * @param day 一天
     * @return {@link ShareFileStar}
     */
    @Select("SELECT sender_id,count(sender_id) as times FROM archived_file WHERE  DATEDIFF(NOW(), archive_date) < #{day} GROUP BY sender_id ORDER BY times desc limit 5")
    List<ShareFileStar> computedFileStar(int day);

    /**
     * 根据书名查询
     *
     * @param name 书名
     * @return {@link List}<{@link ArchivedFile}>
     */
    @Select("SELECT * FROM archived_file WHERE NAME LIKE concat( '%', #{name}, '%' ) AND enabled = 0 AND del_flag = 0 ORDER BY archive_date DESC LIMIT 10")
    List<ArchivedFile> selectBookByName(String name);

    /**
     * 通过md5 重复文件
     *
     * @param md5 md5
     * @return {@link List}<{@link ArchivedFile}>
     */
    @Select("SELECT * FROM archived_file WHERE md5 = #{md5}")
    List<ArchivedFile> selectRepeatFileByMd5(String md5);
}
