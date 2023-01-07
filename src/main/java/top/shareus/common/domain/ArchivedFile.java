package top.shareus.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 存档文件
 *
 * @author 17602
 * @date 2022/11/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchivedFile {
    /**
     * 主键id 为uuid
     */
    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 发件人id
     */
    private Long senderId;

    /**
     * 长度
     */
    private Long size;

    /**
     * MD5
     */
    private String md5;

    /**
     * 启用 0是 1否
     */
    private Integer enabled;

    /**
     * 删除标记 0正常 1删除
     */
    private Integer delFlag;

    /**
     * 源路径
     */
    private String originUrl;

    /**
     * 存档路径
     */
    private String archiveUrl;

    /**
     * 归档日期
     */
    private Date archiveDate;
}
