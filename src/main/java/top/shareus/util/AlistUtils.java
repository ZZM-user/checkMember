package top.shareus.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import redis.clients.jedis.Jedis;
import top.shareus.common.core.constant.AlistConstant;

import java.io.File;
import java.util.HashMap;

/**
 * Alist 工具类
 *
 * @author 17602
 * @date 2022/11/19
 */
public class AlistUtils {

    /**
     * 正则表达式
     */
    private static final String JWT_REGEX = "\"(e.+?)\"";

    public static final String JSON = "application/json; charset=utf-8";

    /**
     * 登录
     *
     * @return {@code String}
     */
    public static String login() {
        // 构建请求体
        HashMap<String, String> map = new HashMap(2) {{
            put("username", AlistConstant.USERNAME);
            put("password", AlistConstant.PASSWORD);
        }};

        HttpResponse response = HttpRequest.post(AlistConstant.LOGIN_API)
                .body(JSONUtil.toJsonPrettyStr(map), JSON)
                .execute().sync();

        if (HttpStatus.HTTP_OK == response.getStatus()) {
            String body = response.body();

            LogUtils.info("登录成功 " + body);
            return ReUtil.get(JWT_REGEX, body, 0).replace("\"", "");
        }

        throw new RuntimeException("Alist登录失败-获取token失败:" + response.body());
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@code String}
     */
    public static String uploadFile(File file) {
        // 文件转二进制
        byte[] bytes = FileUtil.readBytes(file);
        String uploadPath = buildPathOfArchive(file.getName());
        // url编码路径
        String encodePath = URLEncodeUtil.encode(uploadPath);
        LogUtils.info("流：" + bytes.length + "\t编码串：" + encodePath);

        HttpResponse response = null;
        try {
            response = HttpRequest.put(AlistConstant.UPLOAD_FILE_API)
                    .header("file-path", encodePath)
                    .header("authorization", getAuthorization())
                    .body(bytes)
                    .execute().sync();

        } catch (Exception e) {
            System.out.println(e);
        }
        LogUtils.info("上传文件 结束 " + response.body());

        if (HttpStatus.HTTP_OK == response.getStatus()) {
            LogUtils.info(uploadPath + " 上传成功");
            return AlistConstant.DOMAIN + uploadPath;
        }

        throw new RuntimeException("Alist文件上传失败:" + uploadPath + "\t" + response.body());
    }

    /**
     * 创建目录
     *
     * @param dir dir
     * @return {@code Boolean}
     */
    private static void mkdir(String dir) {
        HashMap<String, String> map = new HashMap(1) {{
            put("path", dir);
        }};

        HttpRequest.post(AlistConstant.MKDIR_API)
                .body(JSONUtil.toJsonPrettyStr(map), JSON)
                .header("authorization", getAuthorization())
                .execute().sync();

        LogUtils.info("创建文件夹：" + dir);
    }

    /**
     * 获取 Alist token
     *
     * @return {@code String}
     */
    private static String getAuthorization() {
        LogUtils.info("开始获取Alist授权");
        String token;
        String exists;
        try (Jedis jedis = RedisUtils.getJedis()) {
            token = jedis.get(AlistConstant.AUTH_REDIS_KEY);
            if (StrUtil.isNotBlank(token)) {
                LogUtils.info("无需更新 token");
                return token;
            }

            LogUtils.info("需要更新 token");
            // token已经失效 需要重新登录 登录后再存到redis里
            token = login().trim();
            LogUtils.info("获取Alist Token：" + token);

            exists = jedis.setex(AlistConstant.AUTH_REDIS_KEY, AlistConstant.AUTH_REDIS_EXPIRE, token);
        }

        if (StrUtil.isBlank(exists)) {
            LogUtils.error("Alist 存储登录token失败: " + token);
        } else {
            LogUtils.info("Alist Token：" + exists + " 已存入Redis");
        }

        return token;
    }

    /**
     * 构建 存档 路径
     *
     * @param fileName 文件名称
     * @return {@code String}
     */
    private static String buildPathOfArchive(String fileName) {
        DateTime date = DateUtil.date();
        int year = date.year();
        int month = date.month();
        String archivedPath = AlistConstant.UPLOAD_ALIST_PATH_DOMAIN + year + "/" + (month + 1) + "/" + fileName.trim();

        // 每月、每年的第一天 创建一次目录
        if (date.dayOfMonth() == 1 || date.dayOfYear() == 1) {
            mkdir(archivedPath);
        }

        LogUtils.info("组建归档目录：" + archivedPath);
        return archivedPath;
    }
}
