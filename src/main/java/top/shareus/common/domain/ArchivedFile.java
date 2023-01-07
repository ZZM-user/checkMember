package top.shareus.common.domain;

import java.util.Date;


/**
 * 存档文件
 *
 * @author 17602
 * @date 2022/11/19
 */
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    @Override
    public String toString() {
        return "ArchivedFile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", senderId=" + senderId +
                ", size=" + size +
                ", md5='" + md5 + '\'' +
                ", enabled=" + enabled +
                ", delFlag=" + delFlag +
                ", originUrl='" + originUrl + '\'' +
                ", archiveUrl='" + archiveUrl + '\'' +
                ", archiveDate=" + archiveDate +
                '}';
    }
}
