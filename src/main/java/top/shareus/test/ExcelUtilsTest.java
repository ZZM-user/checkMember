package top.shareus.test;

import org.junit.jupiter.api.Test;
import top.shareus.common.NormalMemberVO;
import top.shareus.util.ExcelUtils;

import java.util.ArrayList;
import java.util.Date;

class ExcelUtilsTest {

    @Test
    void exportExcel() {
        ArrayList<NormalMemberVO> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            NormalMemberVO memberVO = new NormalMemberVO();
            memberVO.setId(0L);
            memberVO.setNameCard("" + i);
            memberVO.setGroupName("" + i);
            memberVO.setGroupId(0L);
            memberVO.setSpecialTitle("");
            memberVO.setAvatarUrl("");
            memberVO.setIsMuted(0);
            memberVO.setMuted("");
            memberVO.setMuteTimeRemaining(0L);
            memberVO.setLastSpeakTimestamp(0L);
            memberVO.setLastSpeakTime(new Date());
            memberVO.setJoinTimestamp(0L);
            memberVO.setJoinTime(new Date());
            memberVO.setRemark("");
            memberVO.setRemark2("备注" + i + "下");
            list.add(memberVO);
        }
        String excel = ExcelUtils.exportMemberDataExcel(list, "测试" + System.currentTimeMillis());
        System.out.println("excel = " + excel);
    }
}