package top.shareus.util;

import cn.hutool.core.date.DateTime;
import net.mamoe.mirai.contact.NormalMember;

/**
 * 普通群成员 常用工具类
 *
 * @author 17602
 * @date 2022/10/29 19:40
 **/
public class NormalMemberUtils {
    
    /**
     * 格式化成员信息
     *
     * @param member 成员
     * @param more   是否更多
     *
     * @return {@link String}
     */
    public static String format(NormalMember member, boolean more) {
        // qq
        return member.getId()
                       // 备注
                       + "-" + member.getRemark()
                       // 群名片
                       + "-" + member.getNameCard()
                       // 所属群组名称
                       + "-" + member.getGroup().getName()
                       // 所属群组号码
                       + (more ? "-" + member.getGroup().getId() : "")
                       // 特殊头衔
                       + "-" + member.getSpecialTitle()
                       // // 头像
                       + (more ? "-" + member.getAvatarUrl() : "")
                       // 是否禁言
                       + (more ? "-" + (member.isMuted() ? "禁言中" : "未禁言") : "")
                       // 秒级时间戳 * 1000 = 毫秒级时间戳
                       // 剩余禁言时长
                       + (more ? "-" + (member.isMuted() ? DateTime.of(member.getMuteTimeRemaining() * 1000L) : "无禁言") : "")
                       // 最后发言时间
                       + "-" + DateTime.of(member.getLastSpeakTimestamp() * 1000L)
                       // 进群时间
                       + "-" + DateTime.of(member.getJoinTimestamp() * 1000L)
                       + "\n--------------------\n";
    }
}
