package top.shareus.common.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： 17602
 * @Date： 2022/8/28 10:12
 * @Desc： 常用群常量
 **/
public class GroupsConstant {
    /**
     * 管理组
     */
    public final static List<Long> ADMIN_GROUPS = new ArrayList<Long>() {{
        add(826992221L);
    }};
    
    /**
     * 资源组
     */
    public final static List<Long> RES_GROUPS = new ArrayList<Long>() {{
        add(879828059L);
    }};
    
    /**
     * 聊天组
     */
    public final static List<Long> CHAT_GROUPS = new ArrayList<Long>() {{
        add(473592372L);
        add(882427723L);
    }};
    
    /**
     * 测试组
     */
    public final static List<Long> TEST_GROUPS = new ArrayList<Long>() {{
        add(591573701L);
    }};
}
