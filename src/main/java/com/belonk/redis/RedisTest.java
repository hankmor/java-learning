package com.belonk.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by sun on 2018/2/23.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class RedisTest {
    //~ Static fields/initializers =====================================================================================

    static final String host = "192.168.0.18";
    static final int port = 6379;
    static Jedis client;
    static String key = "test_";
    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    @Before
    public void getClient() {
        client = new Jedis(host, port);
    }

    @Test
    public void testString() {
        System.out.println("开始写数据");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            client.set(key + i, "v" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("写入完成，耗时：" + (end - start) + "ms");
        // 22778ms
    }

    @Test
    public void testStringBatch() {
        System.out.println("开始写数据");
        long start = System.currentTimeMillis();

        int cnt = 10000;
        int batchCnt = 1000;
        List<String> kvList = new ArrayList<>();

        for (int i = 1; i <= cnt; i++) {
            kvList.add(key + i);
            kvList.add("v" + i);
            if (i % batchCnt == 0) {
                client.mset(kvList.toArray(new String[kvList.size()]));
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("写入完成，耗时：" + (end - start) + "ms");
        // 373ms
    }

    @Test
    public void testMap() {
        System.out.println("开始写数据");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            client.hset(key, "field" + i, "v" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("写入完成，耗时：" + (end - start) + "ms");
        // 33372ms
    }

    @Test
    public void testMapBatch() {
        System.out.println("开始写数据");
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i < 10000; i++) {
            map.put("field" + i, "v" + i);
            if (i % 1000 == 0) {
                client.hmset(key, map);
                map.clear();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("写入完成，耗时：" + (end - start) + "ms");
        // 118ms
    }

    @Test
    public void testRPush1() {
        String data = "{\"i\":" + 11263 + ",\"o\":2,\"r\":0,\"t\":10}";
        client.rpush("es_data_sync", data);
    }

    @Test
    public void testRPush() {
        String[] ss = new String[1000];
        int id = 11263;
        for (int i = 0; i < 1000; i++) {
            id += i;
            String data = "{\"i\":" + id + ",\"o\":2,\"r\":0,\"t\":10}";
            ss[i] = data;
        }
        client.rpush("es_data_sync", ss);
    }
}
