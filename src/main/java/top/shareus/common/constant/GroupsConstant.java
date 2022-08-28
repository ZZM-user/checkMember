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
     * 管理群组、资源群组、聊天群组
     */
    public final static List<Long> ADMIN_GROUPS = new ArrayList<Long>() {{
        add(826992221L);
    }};
    
    public final static List<Long> RES_GROUPS = new ArrayList<Long>() {{
        add(879828059L);
    }};
    
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
