package top.shareus.common.constant;

import cn.hutool.core.util.StrUtil;
import top.shareus.CheckMember;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： 17602
 * @Date： 2022/8/28 10:40
 * @Desc： 关于资源群的违禁词
 **/
public class BanResWordConstant {
    public static final List<String> ZAO = new ArrayList<String>() {{
        add("早");
        add("早啊");
        add("早上好");
    }};
    public static final List<String> WU = new ArrayList<String>() {{
        add("午");
        add("午好");
        add("中午好");
    }};
    public static final List<String> WAN = new ArrayList<String>() {{
        add("晚");
        add("晚啊");
        add("晚安");
        add("晚上好");
    }};
    public static final List<String> OTHER = new ArrayList<String>() {{
        add("");
    }};
    
    
    /**
     * 禁言时长 单位 秒
     * 60 * 60 * 6 = 6h
     */
    public static final int MUTE_SECONDS = 60 * 60 * 6;
    
    /**
     * 是否包含违禁词
     *
     * @param word
     *
     * @return
     */
    public static Boolean hasBanWord(String word) {
        CheckMember.INSTANCE.getLogger().debug("裁决言论：" + word);
        List<String> banWord = new ArrayList<String>() {{
            addAll(ZAO);
            addAll(WU);
            addAll(WAN);
            addAll(OTHER);
        }};
        
        String ban = banWord.stream().filter(word::equals).findAny().orElse(null);
        CheckMember.INSTANCE.getLogger().debug("裁决结果：" + ban);
        return StrUtil.isNotBlank(ban);
    }
}
