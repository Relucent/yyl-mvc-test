package yyl.mvc.plugin.spring.redis.core;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * 订阅者
 */
@FunctionalInterface
public interface Subscriber extends BiConsumer<String, String>, Serializable {

    /**
     * 处理消息
     * @param topic 消息主题
     * @param body 消息内容
     */
    @Override
    void accept(String topic, String body);
}
