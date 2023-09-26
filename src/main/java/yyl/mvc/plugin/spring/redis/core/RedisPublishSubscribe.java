package yyl.mvc.plugin.spring.redis.core;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.github.relucent.base.common.logging.Logger;

/**
 * 消息发布订阅工具类
 */
public class RedisPublishSubscribe implements MessageListener {

    private static final Logger LOG = Logger.getLogger(RedisPublishSubscribe.class);

    private final Map<String, List<Subscriber>> topicSubscribers = new ConcurrentHashMap<>();
    private final StringRedisTemplate redisTemplate;
    private final String channelPrefix;
    private final String channelPattern;

    private final Object topicAccessLock = new byte[0];

    /**
     * 构造函数
     * @param redisTemplate Redis 访问模板（工具）类
     * @param channelPrefix 发布订阅的频道前缀
     */
    public RedisPublishSubscribe(StringRedisTemplate redisTemplate, String channelPrefix) {
        this.redisTemplate = redisTemplate;
        this.channelPrefix = channelPrefix;
        this.channelPattern = channelPrefix + "*";
    }

    /**
     * 订阅消息
     * @param topic 消息主题
     * @param subscriber 消息订阅者
     */
    public void subscribe(String topic, Subscriber subscriber) {
        synchronized (topicAccessLock) {
            topicSubscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(subscriber);
        }
    }

    /**
     * 退订消息
     * @param topic 消息主题
     * @param subscriber 消息订阅者
     */
    public void unsubscribe(String topic, Subscriber subscriber) {
        List<Subscriber> subscribers = topicSubscribers.get(topic);
        if (subscribers == null) {
            return;
        }
        synchronized (topicAccessLock) {
            subscribers.remove(subscriber);
        }
    }

    /**
     * 发布消息
     * @param topic 消息主题
     * @param message 消息内容
     */
    public void publish(String topic, String message) {
        String channelName = getChannelName(topic);
        redisTemplate.convertAndSend(channelName, message);
    }

    /**
     * 获得实际频道名称
     * @param topic 消息主题
     * @return 实际频道名称
     */
    public String getChannelName(String topic) {
        return channelPrefix + topic;
    }

    /**
     * 获得频道匹配模式
     * @return 频道匹配模式
     */
    public String getChannelPattern() {
        return channelPattern;
    }

    /**
     * 继承自 {@code MessageListener}的方法，一般情况应用中不需要主动调用
     * @see MessageListener
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = toString(message.getChannel());
        String body = toString(message.getBody());
        String topic = getTopicName(channel);
        if (topic == null) {
            return;
        }
        synchronized (topicAccessLock) {
            List<Subscriber> subscribers = topicSubscribers.get(topic);
            if (subscribers != null && !subscribers.isEmpty()) {
                for (Subscriber subscriber : subscribers) {
                    try {
                        subscriber.accept(topic, body);
                    } catch (Exception e) {
                        if (e instanceof InterruptedException) {
                            Thread.currentThread().interrupt();
                        }
                        LOG.error("subscriber#accept error!", e);
                    }
                }
            }
        }
    }

    private String toString(byte[] data) {
        return data == null ? null : new String(data, StandardCharsets.UTF_8);
    }

    private String getTopicName(String channel) {
        if (!channel.startsWith(channelPrefix)) {
            return null;
        }
        return channel.substring(channelPrefix.length());
    }
}