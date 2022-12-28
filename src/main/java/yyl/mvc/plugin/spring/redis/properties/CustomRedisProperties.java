package yyl.mvc.plugin.spring.redis.properties;

import java.io.Serializable;

/** _Redis增强配置 */
@SuppressWarnings("serial")
public class CustomRedisProperties implements Serializable {

    // ==============================Fields===========================================
    /** 键前缀 */
    private String keyPrefix = "_y:";
    /** 锁键前缀 */
    private String lockKeyPrefix = "_y_lock:";
    /** 频道前缀 */
    private String channelPrefix = "_y_channel:";

    // ==============================Methods==========================================
    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getLockKeyPrefix() {
        return lockKeyPrefix;
    }

    public void setLockKeyPrefix(String lockKeyPrefix) {
        this.lockKeyPrefix = lockKeyPrefix;
    }

    public String getChannelPrefix() {
        return channelPrefix;
    }

    public void setChannelPrefix(String channelPrefix) {
        this.channelPrefix = channelPrefix;
    }
}