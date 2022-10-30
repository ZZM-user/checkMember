package top.shareus.util;

import cn.hutool.core.util.StrUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 网络请求 常用工具类
 *
 * @author 17602
 * @date： 2022/10/30 14:32
 */
public class HttpUtils {
    
    /**
     * 得到字节数组
     *
     * @param url url
     *
     * @return {@link byte[]}
     */
    public static byte[] getBytesArray(String url) {
        if (!(StrUtil.isNotBlank(url) || StrUtil.startWith(url, "http") || StrUtil.startWith(url, "https"))) {
            return new byte[0];
        }
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().bytes();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return new byte[0];
    }
}
