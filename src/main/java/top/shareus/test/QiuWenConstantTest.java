package top.shareus.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import net.mamoe.mirai.message.data.PlainText;
import org.junit.jupiter.api.Test;
import top.shareus.util.QueryArchivedResFileUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;

class QiuWenConstantTest {

    @org.junit.jupiter.api.Test
    void getExpireTime() {
        long between = DateUtil.between(DateTime.now(), DateUtil.endOfDay(new Date()), DateUnit.SECOND, true);
        System.out.println(between);
        System.out.println(24.0 - (between / 60.0 / 60.0));
        System.out.println(DateTime.now() + "\n" + DateUtil.endOfDay(new Date()));

        System.out.println(DateUtil.nextWeek());
    }

    @Test
    void newQiuWen() {

        List<String> query = new ArrayList() {{
            add("书名：这是个傻福ff\\n作者：李白\\n平台：未知");
            add("求：这是个傻福ffby作者");
            add("求这是个傻福ffby谁啊");
            add("求文:《这是个傻福ff》作者：谁啊");
            add("求文《这是个傻福ff作者谁啊》");
            add("求文 《这是个傻福ff》");
            add("书名：将进酒\n" +
                    "作者：将进酒\n" +
                    "平台：无\n");
        }};

        query.forEach(q -> {
            String bookInfo = QueryArchivedResFileUtils.extractBookInfo(new PlainText(q));
            System.out.println(q + "  [" + bookInfo + "]  " + "这是个傻福ff".equals(bookInfo));
        });
    }

    @Test
    void oldQiuWen() {
        String s = ReUtil.get("[求文](.*)", "书名：静夜思\n作者：李白\n平台：未知", 0);
        System.out.println("s = " + s);

        List<String> query = new ArrayList() {{
            add("求文：这是个傻福ff");
            add("求：这是个傻福ffby作者");
            add("求这是个傻福ffby谁啊");
            add("求文:《这是个傻福ff》作者：谁啊");
            add("求文《这是个傻福ff作者谁啊》");
            add("求文 《这是个傻福ff》");
        }};

        query.forEach(q -> {
            String bookInfo = QueryArchivedResFileUtils.extractBookInfo(new PlainText(q));
            System.out.println(q + "  " + bookInfo + "  " + "这是个傻福ff".equals(bookInfo));
        });
    }

    /**
     * 旧规则 2023-01-07
     *
     * @return {@link String}
     */
    String oldRule() {

        PlainText plainText = new PlainText("");
        // 匹配 《书名》
        String result = ReUtil.get("[求文](.*)", plainText.getContent(), 0);
        if (StrUtil.isEmpty(result)) {
            return "";
        }
        // 移除 书括号 / 求文 之后的内容
        MatchResult matchResultStart = ReUtil.indexOf("(《)|(求文)|(求)", result);
        if (ObjectUtil.isNotNull(matchResultStart)) {
            result = result.substring(matchResultStart.start());
        }

        // 移除 书括号 / 作者 之后的内容
        MatchResult matchResultEnd = ReUtil.indexOf("(》)|(by)|(bY)|(By)|(BY)|(作者)", result);
        if (ObjectUtil.isNotNull(matchResultEnd)) {
            result = result.substring(0, matchResultEnd.start());
        }

        // 最后的替换
        result = result.replaceFirst("(求文)|(求)", "")
                .replace(":", "")
                .replace("：", "")
                .replace("《", "")
                .replace("\n", "")
                .trim();

        return result;
    }
}