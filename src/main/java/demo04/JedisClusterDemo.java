package demo04;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author czq
 * @title
 * @Package demo04
 * @date 2019/5/24 19:48
 */
public class JedisClusterDemo {
    private Logger logger = LoggerFactory.getLogger(JedisClusterDemo.class);
    private Set<HostAndPort> nodeSet = new HashSet<>();
    private JedisCluster jedisCluster = null;
    private List<String> hostPortList = new ArrayList<>();
    @Before
    public void setup(){
        hostPortList.add("192.168.25.155:8000");
        hostPortList.add("192.168.25.155:8001");
        hostPortList.add("192.168.25.155:8002");
        hostPortList.add("192.168.25.155:8003");
        hostPortList.add("192.168.25.155:8004");
        hostPortList.add("192.168.25.155:8005");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        for(String hostPort : hostPortList){
            String[] arr = hostPort.split(":");
            if(arr.length != 2){
                continue;
            }
            nodeSet.add(new HostAndPort(arr[0],Integer.parseInt(arr[1])));
        }
        try {
            jedisCluster = new JedisCluster(nodeSet,1000,7,jedisPoolConfig);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    @Test
    public void testDemo(){
        System.out.println(jedisCluster.get("1"));
    }
    @After
    public void destroy(){
        if (jedisCluster != null){
            jedisCluster.close();
        }
    }
}
