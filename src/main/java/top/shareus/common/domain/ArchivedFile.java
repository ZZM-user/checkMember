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
    
    public Long getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", senderId=").append(senderId);
        sb.append(", size=").append(size);
        sb.append(", md5=").append(md5);
        sb.append(", originUrl=").append(originUrl);
        sb.append(", archiveUrl=").append(archiveUrl);
        sb.append(", archiveDate=").append(archiveDate);
        sb.append("]");
        return sb.toString();
    }
}