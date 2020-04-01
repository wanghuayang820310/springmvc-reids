package com.why.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

/**
 * @Classname RedisClusterAction
 * @Description TODO
 * @Date 2020/3/30 16:10
 * @Created by why
 */
@Controller
//此处省略RequestMapping注解的话就会少一级路径
@RequestMapping("/jedisClustertest")
public class RedisClusterAction {
    @Autowired
    JedisCluster jedisCluster;
    //http://localhost:8080/jedisClustertest/getstr
    @RequestMapping("getstr")
    @ResponseBody
    public  String getstr(){
        jedisCluster.set("clusterstr","whyclusterTest");
        return  jedisCluster.get("clusterstr");

    }
    //http://localhost:8080/jedisClustertest/setstr
    @RequestMapping("setstr")
    @ResponseBody
    public  String setstr(){
        return  jedisCluster.set("clusterstr","whyclusterTest");

    }
}
