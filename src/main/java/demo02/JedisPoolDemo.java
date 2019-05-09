package demo02;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author czq
 * @title
 * @Package demo01
 * @date 2019/5/7 11:13
 */
public class JedisPoolDemo {
    private JedisPool jedisPool;
    @Before
    public void setup(){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        jedisPool = new JedisPool(poolConfig,"192.168.25.152",6379);
    }
    @Test
    public void test02(){
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.set("ggggg","dddd");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
