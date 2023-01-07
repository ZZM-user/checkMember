package top.shareus.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import top.shareus.common.core.constant.ShortUrlConstant;

import java.util.HashMap;

/**
 * 短链接 工具类
 *
 * @author 17602
 * @date 2022/11/20
 */
public class ShortUrlUtils {

    /**
     * 正则表达式
     */
    private static final String REGEX = "(http+.*)";
    /**
     * json
     */
    private static final String JSON = "application/json; charset=utf-8";

    /**
     * 生成短url
     *
     * @param longUrl 长url
     * @return {@code String}
     */
    public static String generateShortUrl(String longUrl) {
        HashMap<String, String> map = new HashMap(3) {{
            put("url", longUrl);
            put("expiry", DateUtil.format(DateUtil.tomorrow(), "yyyy-MM-dd"));
            put("debrowser", new HashMap(2) {{
                put("type", "1");
                put("app", "1,2");
            }});
        }};

        HttpResponse response = HttpRequest.post(ShortUrlConstant.ADD_API)
                .body(JSONUtil.toJsonPrettyStr(map), JSON)
                .header("authorization", ShortUrlConstant.APIKEY)
                .execute().sync();

        if (response.getStatus() == HttpStatus.HTTP_OK) {
            String body = response.body();
            LogUtils.info("获取短连接成功：" + body);

            String shortUrl = ReUtil.get(REGEX, body, 0).trim();
            shortUrl = shortUrl.substring(0, shortUrl.length() - 2)
                    .replace("\\", "");

            LogUtils.info("生产短连接成功：" + shortUrl);
            return shortUrl;
        }

        LogUtils.error("生产短连接失败！" + longUrl);

        throw new RuntimeException("生产短连接失败！");
    }
}
