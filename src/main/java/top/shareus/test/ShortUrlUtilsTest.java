package top.shareus.test;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class ShortUrlUtilsTest {

    @Test
    void generateShortUrl() {
        HashMap<String, String> map = new HashMap(3) {{
            put("url", "longUrl");
            put("expiry", DateUtil.format(DateUtil.tomorrow(), "yyyy-MM-dd"));
            put("debrowser", new HashMap(2) {{
                put("type", "1");
                put("app", "1,2");
            }});
        }};

        System.out.println(map);
    }
}