package top.shareus.common.domain;

/**
 * 共享文件明星
 *
 * @author zhaojl
 * @date 2022/11/27
 */
public class ShareFileStar {
    Long senderId;
    Long times;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }
}
