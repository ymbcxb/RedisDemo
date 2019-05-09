package demo01;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author czq
 * @title
 * @Package demo01
 * @date 2019/5/7 11:02
 */
public class JdeisDemo {

    private Jedis jedis;
    @Before
    public void setup(){
        jedis = new Jedis("192.168.25.152",6379);
    }
    @Test
    public void test01(){
        System.out.println(jedis.set("hello","world"));
        jedis.set("k1","v1");
    }
}
