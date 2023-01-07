package top.shareus.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import net.mamoe.mirai.message.data.PlainText;
import org.junit.jupiter.api.Test;
import top.shareus.event.QueryArchivedResFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class QiuWenConstantTest {

    @org.junit.jupiter.api.Test
    void getExpireTime() {
        long between = DateUtil.between(DateTime.now(), DateUtil.endOfDay(new Date()), DateUnit.SECOND, true);
        System.out.println(between);
        System.out.println(24.0 - (between / 60.0 / 60.0));
        System.out.println(DateTime.now() + "\n" + DateUtil.endOfDay(new Date()));
    }

    @Test
    void QiuWen() {
        QueryArchivedResFile queryArchivedResFile = new QueryArchivedResFile();

        List<String> query = new ArrayList() {{
            add("求文：这是个傻福ff");
            add("求：这是个傻福ffby作者");
            add("求这是个傻福ffby谁啊");
            add("求文:《这是个傻福ff》作者：谁啊");
            add("求文《这是个傻福ff作者谁啊》");
            add("求文 《这是个傻福ff》");
        }};

        query.forEach(q -> {
            String bookInfo = queryArchivedResFile.extractBookInfo(new PlainText(q));
            System.out.println(q + "  " + bookInfo + "  " + "这是个傻福ff".equals(bookInfo));
        });

    }
}