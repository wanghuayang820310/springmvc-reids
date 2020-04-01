package com.why.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Classname RedisUtil
 * @Description TODO
 * @Date 2020/3/29 9:27
 * @Created by why
 */
public class RedisUtil {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    @Qualifier("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Qualifier("redisTemplate")
    protected RedisTemplate<Serializable, Serializable> redisTemplateSerializable;


    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @return
     * @paramby 要增加几(大于0)
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key 键
     * @return
     * @paramby 要减少几(小于0)
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     * @paramtime 时间(秒)
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @return
     * @paramkey 键
     * @paramvalue 值
     * @paramtime 时间(秒)
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    // ===============================sorted set=================================

    /**
     * 向有序集合添加一个成员的
     * <p>
     * ZADD key score1 member1 [score2 member2]
     */
    public boolean zadd(String key, Object member, double score, long time) {
        try {
            redisTemplate.opsForZSet().add(key, member, score);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     */
    public Set<Object> zRangeByScore(String key, double minScore, double maxScore) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * ZSCORE key member
     * 返回有序集中，成员的分数值
     */
    public Double zscore(String key, Object member) {
        try {
            return redisTemplate.opsForZSet().score(key, member);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * ZRANK key member 返回有序集合中指定成员的索引
     */
    public Long zrank(String key, Object member) {
        try {
            return redisTemplate.opsForZSet().rank(key, member);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * Zscan 迭代有序集合中的元素（包括元素成员和元素分值）
     */
    public Cursor<ZSetOperations.TypedTuple<Object>> zscan(String key) {
        try {
            Cursor<ZSetOperations.TypedTuple<Object>> cursor = redisTemplate.opsForZSet().scan(key, ScanOptions.NONE);
            return cursor;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

//
//
//    /**
//     * 缓存基本的对象，Integer、String、实体类等
//     *
//     * @param key
//     *            缓存的键值
//     * @param value
//     *            缓存的值
//     * @return 缓存的对象
//     */
//    public void setCacheObject(String key, Object value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    /**
//     * 获得缓存的基本对象。
//     *
//     * @param key
//     *            缓存键值
//     * @param-operation
//     * @return 缓存键值对应的数据
//     */
//    public Object getCacheObject(String key/* ,ValueOperations<String,T> operation */) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    /**
//     * 缓存List数据
//     *
//     * @param key
//     *            缓存的键值
//     * @param dataList
//     *            待缓存的List数据
//     * @return 缓存的对象
//     */
//    public Object setCacheList(String key, List<Object> dataList) {
//        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
//        if (null != dataList) {
//            int size = dataList.size();
//            for (int i = 0; i < size; i++) {
//                listOperation.rightPush(key, dataList.get(i));
//            }
//        }
//        return listOperation;
//    }
//
//    /**
//     * 获得缓存的list对象
//     *
//     * @param key
//     *            缓存的键值
//     * @return 缓存键值对应的数据
//     */
//    public List<Object> getCacheList(String key) {
//        List<Object> dataList = new ArrayList<Object>();
//        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
//        Long size = listOperation.size(key);
//
//        for (int i = 0; i < size; i++) {
//            dataList.add(listOperation.leftPop(key));
//        }
//        return dataList;
//    }
//
//    /**
//     * 获得缓存的list对象 @Title: range @param @param key @param @param start @param @param
//     * end @param @return @return List<T> 返回类型 @throws
//     */
//    public List<Object> range(String key, long start, long end) {
//        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
//        return listOperation.range(key, start, end);
//    }
//
//    /**
//     * list集合长度
//     *
//     * @param key
//     * @return
//     */
//    public Long listSize(String key) {
//        return redisTemplate.opsForList().size(key);
//    }
//
//    /**
//     * 覆盖操作,将覆盖List中指定位置的值
//     *
//     * @param key
//     * @param-int
//     *            index 位置
//     * @param-String
//     *            value 值
//     * @return 状态码
//     */
//    public void listSet(String key, int index, Object obj) {
//        redisTemplate.opsForList().set(key, index, obj);
//    }
//
//    /**
//     * 向List尾部追加记录
//     *
//     * @param-String
//     *            key
//     * @param-String
//     *            value
//     * @return 记录总数
//     */
//    public long leftPush(String key, Object obj) {
//        return redisTemplate.opsForList().leftPush(key, obj);
//    }
//
//    /**
//     * 向List头部追加记录
//     *
//     * @param-String
//     *            key
//     * @param-String
//     *            value
//     * @return 记录总数
//     */
//    public long rightPush(String key, Object obj) {
//        return redisTemplate.opsForList().rightPush(key, obj);
//    }
//
//    /**
//     * 算是删除吧，只保留start与end之间的记录
//     *
//     * @param-String
//     *            key
//     * @param-int
//     *            start 记录的开始位置(0表示第一条记录)
//     * @param-int
//     *            end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
//     * @return 执行状态码
//     */
//    public void trim(String key, int start, int end) {
//        redisTemplate.opsForList().trim(key, start, end);
//    }
//
//    /**
//     * 删除List中c条记录，被删除的记录值为value
//     *
//     * @param-String
//     *            key
//     * @param-int
//     *            c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
//     * @param-Object
//     *            obj 要匹配的值
//     * @return 删除后的List中的记录数
//     */
//    public long remove(String key, long i, Object obj) {
//        return redisTemplate.opsForList().remove(key, i, obj);
//    }
//
//    /**
//     * 缓存Set
//     *
//     * @param key
//     *            缓存键值
//     * @param dataSet
//     *            缓存的数据
//     * @return 缓存数据的对象
//     */
//    public BoundSetOperations<String, Object> setCacheSet(String key, Set<Object> dataSet) {
//        BoundSetOperations<String, Object> setOperation = redisTemplate.boundSetOps(key);
//        /*
//         * T[] t = (T[]) dataSet.toArray(); setOperation.add(t);
//         */
//
//        Iterator<Object> it = dataSet.iterator();
//        while (it.hasNext()) {
//            setOperation.add(it.next());
//        }
//        return setOperation;
//    }
//
//    /**
//     * 获得缓存的set
//     *
//     * @param key
//     * @param-operation
//     * @return
//     */
//    public Set<Object> getCacheSet(String key/* ,BoundSetOperations<String,T> operation */) {
//        Set<Object> dataSet = new HashSet<Object>();
//        BoundSetOperations<String, Object> operation = redisTemplate.boundSetOps(key);
//
//        Long size = operation.size();
//        for (int i = 0; i < size; i++) {
//            dataSet.add(operation.pop());
//        }
//        return dataSet;
//    }
//
//    /**
//     * 缓存Map
//     *
//     * @param key
//     * @param dataMap
//     * @return
//     */
//    public int setCacheMap(String key, Map<String, Object> dataMap) {
//        if (null != dataMap) {
//            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
//                /*
//                 * System.out.println("Key = " + entry.getKey() + ", Value = " +
//                 * entry.getValue());
//                 */
//                if (hashOperations != null) {
//                    hashOperations.put(key, entry.getKey(), entry.getValue());
//                } else {
//                    return 0;
//                }
//            }
//        } else {
//            return 0;
//        }
//        return dataMap.size();
//    }
//
//    /**
//     * 获得缓存的Map
//     *
//     * @param-key
//     * @param-hashOperation
//     * @return
//     */
//    public Map<Object, Object> getCacheMap(String key/* ,HashOperations<String,String,T> hashOperation */) {
//        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
//        /* Map<String, T> map = hashOperation.entries(key); */
//        return map;
//    }
//
//    /**
//     * 缓存Map
//     *
//     * @param key
//     * @param dataMap
//     * @return
//     */
//    public void setCacheIntegerMap(String key, Map<Integer, Object> dataMap) {
//        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//        if (null != dataMap) {
//            for (Map.Entry<Integer, Object> entry : dataMap.entrySet()) {
//                /*
//                 * System.out.println("Key = " + entry.getKey() + ", Value = " +
//                 * entry.getValue());
//                 */
//                hashOperations.put(key, entry.getKey(), entry.getValue());
//            }
//
//        }
//    }
//
//    /**
//     * 获得缓存的Map
//     *
//     * @param-key
//     * @param-hashOperation
//     * @return
//     */
//    public Map<Object, Object> getCacheIntegerMap(String key/* ,HashOperations<String,String,T> hashOperation */) {
//        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
//        /* Map<String, T> map = hashOperation.entries(key); */
//        return map;
//    }
//
//    /**
//     * 从hash中删除指定的存储
//     *
//     * @param-String
//     * @return 状态码，1成功，0失败
//     */
//    public long deleteMap(String key) {
//        redisTemplate.setEnableTransactionSupport(true);
//        return redisTemplate.opsForHash().delete(key);
//    }
//
//    /**
//     * 设置过期时间
//     *
//     * @param-key
//     * @param-time
//     * @param-unit
//     * @return
//     */
//    public boolean expire(String key, long time, TimeUnit unit) {
//        return redisTemplate.expire(key, time, unit);
//    }
//
//    /**
//     * increment
//     *
//     * @param-key
//     * @param-step
//     * @return
//     */
//    public long increment(String key, long step) {
//        return redisTemplate.opsForValue().increment(key, step);
//    }
//
//    // redisTemplateSerializable
//
//    /**
//     * 删除redis的所有数据
//     */
//    /*
//     * @SuppressWarnings({"unchecked", "rawtypes"}) public String flushDB() { return
//     * redisTemplateSerializable.execute(new RedisCallback() { public String
//     * doInRedis(RedisConnection connection) throws DataAccessException {
//     * connection.flushDb(); return "ok"; } }); }
//     */
//
//    public Object del(final byte[] key) {
//        return redisTemplateSerializable.execute(new RedisCallback<Object>() {
//            public Long doInRedis(RedisConnection connection) {
//                return connection.del(key);
//            }
//        });
//    }
//
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    public byte[] get(final byte[] key) {
//        return (byte[]) redisTemplateSerializable.execute(new RedisCallback() {
//            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
//                return connection.get(key);
//            }
//        });
//    }
//
//    /**
//     * @param key
//     * @param value
//     * @param liveTime
//     */
//    public void set(final byte[] key, final byte[] value, final long liveTime) {
//        redisTemplateSerializable.execute(new RedisCallback<Object>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                connection.set(key, value);
//                if (liveTime > 0) {
//                    connection.expire(key, liveTime);
//                }
//                return 1L;
//            }
//        });
//    }
}
