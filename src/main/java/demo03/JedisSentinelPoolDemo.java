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
    private static JedisPoolConfig config = null;
    static {
        config = new JedisPoolConfig();
        //设置最大连接总数
        config.setMaxTotal(300);
        //设置最大空闲数
        config.setMaxIdle(50);
        //设置最小空闲数
        config.setMinIdle(8);
        config.setMaxWaitMillis(10000);
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);
        //在空闲时检查有效性, 默认false
        config.setTestOnReturn(true);
        //是否启用pool的jmx管理功能, 默认true
        config.setJmxEnabled(true);
        //Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //是否启用后进先出, 默认true
        config.setLifo(true);
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(30000);
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(10);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);
        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        config.setBlockWhenExhausted(true);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
        config.setSoftMinEvictableIdleTimeMillis(1800000);
//        Set<String> set = new HashSet<>();
//        //连接地址以及端口号,有多个就一次增加
//        set.add("192.168.1.101:26379");
//        jedisSentinelPool = new JedisSentinelPool("mymaster", set, config);
    }
    @Before
    public void setup(){
        sentine.add("192.168.25.155:26379");
        sentine.add("192.168.25.155:26380");
        sentine.add("192.168.25.155:26381");
        jedisSentinelPool = new JedisSentinelPool(masterName,sentine,config);
    }
    @Test
    public void test01(){
        while(true){
            try(Jedis jedis = jedisSentinelPool.getResource()){
                int index =  new Random().nextInt(100000);
                String key = "k-"+index;
                String value = "v-"+index;
                jedis.set(key,value);
                logger.info("{} value is {}",key,jedis.get(key));
                TimeUnit.MILLISECONDS.sleep(10);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }
}
