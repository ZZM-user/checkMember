package top.shareus.util;

import cn.hutool.core.util.ObjectUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * Redis工具类
 *
 * @author 17602
 * @date 2022/11/19
 */
public class RedisUtils {

    private static JedisPool jedisPool = null;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(80);
        jedisPoolConfig.setMaxIdle(30);
        jedisPoolConfig.setMinIdle(8);
        jedisPoolConfig.setMaxWait(Duration.ofMillis(3000));
        jedisPoolConfig.setNumTestsPerEvictionRun(-1);
        jedisPool = new JedisPool(jedisPoolConfig, "124.220.67.51", 6379, null, "ZJL20010516");
    }

    public static Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();

        if (ObjectUtil.isNull(jedis)) {
            LogUtils.error("获取Jedis实例失败！");
        }
        jedis.select(10);
        return jedis;
    }
}
