package top.shareus.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import top.shareus.common.NormalMemberVO;

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
     * @return {@link String}
     */
    public static String format(NormalMemberVO member, boolean more) {
        // qq
        return member.getId()
                // 备注
                + "-" + member.getRemark()
                // 群名片
                + (StrUtil.isNotBlank(member.getNameCard()) ? ("-" + member.getNameCard()) : "")
                // 所属群组名称
                + "-" + member.getGroupName()
                // 所属群组号码
                + (more ? "-" + member.getGroupId() : "")
                // 特殊头衔
                + (StrUtil.isNotBlank(member.getSpecialTitle()) ? ("-" + member.getSpecialTitle()) : "")
                // // 头像
                + (more ? "-" + member.getAvatarUrl() + " " : "")
                // 是否禁言
                + (more ? "-" + member.getMuted() : ""
                // 秒级时间戳 * 1000 = 毫秒级时间戳
                // 剩余禁言时长
                + (more ? member.getMuteTimeRemaining() : "")
                // 最后发言时间
                + "-" + DateTime.of(member.getLastSpeakTimestamp() * 1000L)
                // 进群时间
                + "-" + DateTime.of(member.getJoinTimestamp() * 1000L)
                + "\n----------------------\n");
    }
}
