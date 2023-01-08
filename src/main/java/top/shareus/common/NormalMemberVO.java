package top.shareus.common;

import cn.hutool.core.date.DateUtil;
import net.mamoe.mirai.contact.NormalMember;

import java.util.Date;

/**
 * 正常成员 VO
 *
 * @author zhaojl
 * @date 2023/01/08
 */
public class NormalMemberVO {
    private Long id;
    private String nameCard;
    private String nick;
    private Long groupId;
    private String groupName;
    private String specialTitle;
    private String avatarUrl;
    private Integer isMuted;
    private String muted;
    private Long muteTimeRemaining;
    private Long lastSpeakTimestamp;
    private Date lastSpeakTime;
    private Long joinTimestamp;
    private Date joinTime;
    private String remark;
    private String remark2;

    public static NormalMemberVO toMemberVO(NormalMember member) {
        NormalMemberVO memberVO = new NormalMemberVO();

        memberVO.setId(member.getId());
        memberVO.setNameCard(member.getNameCard());
        memberVO.setNick(member.getNick());
        memberVO.setRemark(member.getRemark());
        memberVO.setAvatarUrl(member.getAvatarUrl());
        memberVO.setSpecialTitle(member.getSpecialTitle());
        memberVO.setMuted(member.isMuted() ? "禁言中" : "未禁言");
        memberVO.setGroupName(member.getGroup().getName());
        memberVO.setGroupId(member.getGroup().getId());
        memberVO.setJoinTime(DateUtil.date(member.getJoinTimestamp() * 1000L));
        memberVO.setLastSpeakTime(DateUtil.date(member.getLastSpeakTimestamp() * 1000L));

        return memberVO;
    }

    public static NormalMemberVO toMemberVO(NormalMember member, String remark2) {
        NormalMemberVO normalMemberVO = toMemberVO(member);
        normalMemberVO.setRemark2(remark2);
        return normalMemberVO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSpecialTitle() {
        return specialTitle;
    }

    public void setSpecialTitle(String specialTitle) {
        this.specialTitle = specialTitle;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getIsMuted() {
        return isMuted;
    }

    public void setIsMuted(Integer isMuted) {
        this.isMuted = isMuted;
    }

    public String getMuted() {
        return muted;
    }

    public void setMuted(String muted) {
        this.muted = muted;
    }

    public Long getMuteTimeRemaining() {
        return muteTimeRemaining;
    }

    public void setMuteTimeRemaining(Long muteTimeRemaining) {
        this.muteTimeRemaining = muteTimeRemaining;
    }

    public Long getLastSpeakTimestamp() {
        return lastSpeakTimestamp;
    }

    public void setLastSpeakTimestamp(Long lastSpeakTimestamp) {
        this.lastSpeakTimestamp = lastSpeakTimestamp;
    }

    public Date getLastSpeakTime() {
        return lastSpeakTime;
    }

    public void setLastSpeakTime(Date lastSpeakTime) {
        this.lastSpeakTime = lastSpeakTime;
    }

    public Long getJoinTimestamp() {
        return joinTimestamp;
    }

    public void setJoinTimestamp(Long joinTimestamp) {
        this.joinTimestamp = joinTimestamp;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    @Override
    public String toString() {
        return "NormalMemberVO{" +
                "id=" + id +
                ", nameCard='" + nameCard + '\'' +
                ", nickName='" + nick + '\'' +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", specialTitle='" + specialTitle + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", isMuted=" + isMuted +
                ", muted='" + muted + '\'' +
                ", muteTimeRemaining=" + muteTimeRemaining +
                ", lastSpeakTimestamp=" + lastSpeakTimestamp +
                ", lastSpeakTime=" + lastSpeakTime +
                ", joinTimestamp=" + joinTimestamp +
                ", joinTime=" + joinTime +
                ", remark='" + remark + '\'' +
                ", remark2='" + remark2 + '\'' +
                '}';
    }
}
