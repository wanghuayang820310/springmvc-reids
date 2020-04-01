package com.why.cluster;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @Classname JedisClusterFactory
 * @Description TODO
 * @Date 2020/3/30 14:12
 * @Created by why
 */
@Repository(value = "jedisClusterFactory")
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

    private String address;
    private String addressKeyPrefix;
    private JedisCluster jedisCluster;
    private String timeout;
    private String maxRedirections;
    private GenericObjectPoolConfig genericObjectPoolConfig;

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxWaitMillis(1000);
        genericObjectPoolConfig.setMaxTotal(20000);
        genericObjectPoolConfig.setMaxIdle(20);
        genericObjectPoolConfig.setTestOnBorrow(true);
        return genericObjectPoolConfig;
    }

    private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    @Override
    public JedisCluster getObject() throws Exception {
        afterPropertiesSet();
        return jedisCluster;
    }

    @Override
    public Class<? extends JedisCluster> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public String getAddress() {
        return address;
    }

    private Set<HostAndPort> parseHostAndPort() throws Exception {
        try {
//            this.setAddress("192.168.10.204:6379,192.168.10.204:6380,192.168.10.204:6381,192.168.10.204:6389,192.168.10.204:6390,192.168.10.204:6391");
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            System.out.println(this.address);
            if (address != null) {
                String[] addressArr = address.trim().split(",");
                for (String addressStr : addressArr) {
                    String[] ipAndPort = addressStr.trim().split(":");
                    HostAndPort hap = new HostAndPort(ipAndPort[0].trim(), Integer.parseInt(ipAndPort[1].trim()));
                    haps.add(hap);
                }
            }
            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("解析 jedis 配置文件失败", ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
        if (haps != null) {
            if (genericObjectPoolConfig == null) {
                genericObjectPoolConfig = getGenericObjectPoolConfig();
            }
            jedisCluster = new JedisCluster(haps, genericObjectPoolConfig);
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public void setAddressConfig(Resource addressConfig) {
//        this.addressConfig = addressConfig;
//    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public void setMaxRedirections(String maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public void setAddressKeyPrefix(String addressKeyPrefix) {
        this.addressKeyPrefix = addressKeyPrefix;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }
}
