package yyl.mvc.plugin.spring.redis.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import yyl.mvc.common.convert.ConvertUtil;
import yyl.mvc.plugin.spring.redis.constant.RedisInfoDescriptions;
import yyl.mvc.plugin.spring.redis.info.RedisInfoEntry;
import yyl.mvc.plugin.spring.redis.properties.CustomRedisProperties;

/**
 * _Redis 工具实例类，为{@code RedisDs}提供支持
 */
public class RedisDsComponent {

    // ==============================Fields===========================================
    /** _Redis访问模板类 */
    private final StringRedisTemplate redisTemplate;
    /** 订阅发布工具 */
    private final RedisPublishSubscribe pubSub;
    /** 存储键前缀 */
    private final String keyPrefix;
    /** 分布式锁存储键前缀 */
    private final String lockKeyPrefix;
    /** 分布式锁存储表 */
    private final Map<String, RedisLock> distributedLockMap = new ConcurrentHashMap<>();

    // ==============================Constructor======================================
    /**
     * 构造函数
     * @param redisTemplate _Redis访问类
     * @param pubSub 订阅发布类
     * @param keyPrefix 键前缀
     */
    public RedisDsComponent(StringRedisTemplate redisTemplate, RedisPublishSubscribe pubSub, CustomRedisProperties properties) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = properties.getKeyPrefix();
        this.lockKeyPrefix = properties.getLockKeyPrefix();
        this.pubSub = pubSub;
    }

    // ==============================Methods==========================================
    /**
     * 设置指定键的值
     * @param key 键
     * @param value 值
     */
    public void setString(String key, String value) {
        String actualKey = getActualKey(key);
        redisTemplate.opsForValue().set(actualKey, value);
    }

    /**
     * 设置指定键的值
     * @param key 键
     * @param value 值
     * @param expire 有效时间
     */
    public void setString(String key, String value, Duration expire) {
        String actualKey = getActualKey(key);
        if (expire == null) {
            redisTemplate.opsForValue().set(actualKey, value);
        } else {
            redisTemplate.opsForValue().set(actualKey, value, expire.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 获取给定键的值
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        String actualKey = getActualKey(key);
        return redisTemplate.opsForValue().get(actualKey);
    }

    /**
     * 删除值
     * @param key 需要删除的键
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        String actualKey = getActualKey(key);
        return redisTemplate.delete(actualKey);
    }

    /**
     * 设置键的过期时间
     * @param key 键
     * @param expire 有效时间
     */
    public void expire(String key, Duration expire) {
        String actualKey = getActualKey(key);
        redisTemplate.expire(actualKey, expire);
    }

    /**
     * 以秒为单位返回 key 的剩余过期时间。 <br>
     * 当 key 不存在时，返回 -2；当 key 存在但没有设置剩余生存时间时，返回 -1； 否则，以秒为单位，返回 key 的剩余生存时间。 <br>
     * @param key 键
     * @return 剩余过期时间 （单位秒）
     */
    public long ttl(String key) {
        String actualKey = getActualKey(key);
        return redisTemplate.getExpire(actualKey);
    }

    /**
     * 检测键是否存在
     * @param key 存储键
     * @return 如果键存在返回true，否则返回false
     */
    public boolean hasKey(String key) {
        String actualKey = getActualKey(key);
        return redisTemplate.hasKey(actualKey);
    }

    /**
     * 订阅消息
     * @param topic 消息主题
     * @param subscriber 消息订阅者
     */
    public void subscribe(String topic, Subscriber subscriber) {
        pubSub.subscribe(topic, subscriber);
    }

    /**
     * 退订消息
     * @param topic 消息主题
     * @param subscriber 消息订阅者
     */
    public void unsubscribe(String topic, Subscriber subscriber) {
        pubSub.unsubscribe(topic, subscriber);
    }

    /**
     * 发布消息
     * @param topic 消息主题
     * @param message 消息内容
     */
    public void publish(String topic, String message) {
        pubSub.publish(topic, message);
    }

    /**
     * 获得分布式锁
     * @param name 锁名称
     * @return 分布式锁
     */
    public Lock getLock(String name) {
        return distributedLockMap.computeIfAbsent(name, k -> new RedisLock(redisTemplate, pubSub, lockKeyPrefix, name));
    }

    /**
     * 返回当前库的 KEY 的数量 <br>
     * 不能在在管道/事务中使用，否则会返回 {@code null}<br>
     * @return 当前库的 KEY的数量
     */
    public Long dbSize() {
        return redisTemplate.execute((RedisCallback<Long>) (connection -> {
            return connection.dbSize();
        }));
    }

    /**
     * 获得 _Redis详细信息
     * @return _Redis 详细条目列表
     */
    public List<RedisInfoEntry> getRedisInfo() {
        return redisTemplate.execute((RedisCallback<List<RedisInfoEntry>>) (connection -> {
            List<RedisInfoEntry> info = new ArrayList<>();
            Properties properties = connection.info();
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String name = ConvertUtil.toString(entry.getKey(), "");
                String value = ConvertUtil.toString(entry.getValue(), "");
                String description = RedisInfoDescriptions.getInfoDescription(name);
                RedisInfoEntry infoEntry = new RedisInfoEntry();
                infoEntry.setName(name);
                infoEntry.setValue(value);
                infoEntry.setDescription(description);
                info.add(infoEntry);
            }
            return info;
        }));
    }

    // ==============================ToolMethods======================================
    /**
     * 获得键的绝对名称
     * @param key 键
     * @return 键的绝对名称
     */
    private String getActualKey(String key) {
        return keyPrefix + key;
    }
}
