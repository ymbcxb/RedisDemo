package demo03;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.sql.Time;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author czq
 * @title
 * @Package demo03
 * @date 2019/5/19 16:50
 */
public class JedisSentinelPoolDemo {
    private static Logger logger = LoggerFactory.getLogger(JedisSentinelPoolDemo.class);
    private String masterName = "mymaster";
    private Set<String> sentine = new HashSet<String>();
    private JedisSentinelPool jedisSentinelPool;
    @Before
    public void setup(){
        sentine.add("192.168.25.155:26379");
        sentine.add("192.168.25.155:26380");
        sentine.add("192.168.25.155:26381");
        jedisSentinelPool = new JedisSentinelPool(masterName,sentine);
    }
    @Test
    public void test01(){
        int counter = 0;
        while(true){
            counter++;
            try(Jedis jedis = jedisSentinelPool.getResource()){
                int index =  new Random().nextInt(100000);
                String key = "k-"+index;
                String value = "v-"+index;
                jedis.set(key,value);
                if(counter % 100 == 0){
                    logger.info("{} value is {}",key,jedis.get(key));
                }
                TimeUnit.MILLISECONDS.sleep(10);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }
}
