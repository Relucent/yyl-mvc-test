package yyl.mvc.plugin.spring.redis.core;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

import yyl.mvc.plugin.expection.ErrorType;
import yyl.mvc.plugin.expection.ExceptionHelper;
import yyl.mvc.plugin.spring.redis.info.RedisInfoEntry;

/**
 * _Redis 工具类，使用静态方法对常用操作进行封装，简化_Redis的使用。
 */
public class RedisDs {

    // ==============================Fields===========================================
    /** _Redis访问模板类 */
    private static final AtomicReference<RedisDsComponent> COMPONENT = new AtomicReference<>();

    // ==============================Constructor======================================
    protected RedisDs() {
    }

    // ==============================Methods==========================================
    /**
     * 设置指定键的值
     * @param key 键
     * @param value 值
     */
    public static void setString(String key, String value) {
        getComponent().setString(key, value);
    }

    /**
     * 设置指定键的值
     * @param key 键
     * @param value 值
     * @param expire 有效时间
     */
    public static void setString(String key, String value, Duration expire) {
        getComponent().setString(key, value, expire);
    }

    /**
     * 获取给定键的值
     * @param key 键
     * @return 值
     */
    public static String getString(String key) {
        return getComponent().getString(key);
    }

    /**
     * 删除值
     * @param key 需要删除的键
     * @return 是否删除成功
     */
    public static boolean delete(String key) {
        return getComponent().delete(key);
    }

    /**
     * 设置键的过期时间
     * @param key 键
     * @param expire 有效时间
     */
    public static void expire(String key, Duration expire) {
        getComponent().expire(key, expire);
    }

    /**
     * 以秒为单位返回 key 的剩余过期时间。 <br>
     * 当 key 不存在时，返回 -2；当 key 存在但没有设置剩余生存时间时，返回 -1； 否则，以秒为单位，返回 key 的剩余生存时间。 <br>
     * @param key 键
     * @return 剩余过期时间 （单位秒）
     */
    public static long ttl(String key) {
        return getComponent().ttl(key);
    }

    /**
     * 检测键是否存在
     * @param key 存储键
     * @return 如果键存在返回true，否则返回false
     */
    public static boolean hasKey(String key) {
        return getComponent().hasKey(key);
    }

    /**
     * 订阅消息
     * @param topic 消息主题
     * @param subscriber 消息订阅者
     */
    public static void subscribe(String topic, Subscriber subscriber) {
        getComponent().subscribe(topic, subscriber);
    }

    /**
     * 退订消息
     * @param topic 消息主题
     * @param subscriber 消息消费者
     */
    public static void unsubscribe(String topic, Subscriber subscriber) {
        getComponent().unsubscribe(topic, subscriber);
    }

    /**
     * 发布消息
     * @param topic 消息主题
     * @param message 消息内容
     */
    public static void publish(String topic, String message) {
        getComponent().publish(topic, message);
    }

    /**
     * 获得分布式锁
     * @param name 锁名称
     * @return 分布式锁
     */
    public static Lock getLock(String name) {
        return getComponent().getLock(name);
    }

    /**
     * 获得 _Redis 详细信息
     * @return _Redis 信息项列表
     */
    public static List<RedisInfoEntry> getRedisInfo() {
        return getComponent().getRedisInfo();
    }

    // ==============================ToolMethods======================================
    /**
     * 获取组件实例
     * @return 服务组件实例
     */
    private static RedisDsComponent getComponent() {
        RedisDsComponent component = COMPONENT.get();
        if (component == null) {
            throw ExceptionHelper.error(ErrorType.DEFAULT, "RedisDs is not initialized. Please check the configuration and properties!");
        }
        return component;
    }

    /**
     * 设置工具实例类
     * @param component 工具实例类
     */
    protected static void setComponent(RedisDsComponent component) {
        COMPONENT.set(component);
    }
}
