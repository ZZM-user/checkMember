package top.shareus.common.domain;

import java.util.Date;

/**
 * 查询日志
 *
 * @author zhaojl
 * @date 2023/01/07
 */
public class QueryLog {
    private Long id;

    /**
     * 求文人名字
     */
    private String senderName;

    /**
     * 求文人QQ
     */
    private Long senderId;

    /**
     * 求文内容
     */
    private String content;

    /**
     * 提取书名
     */
    private String extract;

    /**
     * 状态 （0成功 1等待 2失败）
     */
    private Integer status;

    /**
     * 回复人id (0机器人 / 其他人)
     */
    private Long answerId;

    /**
     * 结果
     */
    private String result;

    /**
     * 查询时间
     */
    private Date sendTime;

    /**
     * 完成时间
     */
    private Date finishTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "QueryLog{" +
                "id=" + id +
                ", senderName='" + senderName + '\'' +
                ", senderId=" + senderId +
                ", content='" + content + '\'' +
                ", extract='" + extract + '\'' +
                ", status=" + status +
                ", answerId=" + answerId +
                ", result='" + result + '\'' +
                ", sendTime=" + sendTime +
                ", finishTime=" + finishTime +
                '}';
    }
}
