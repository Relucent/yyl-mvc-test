package yyl.mvc.plugin.spring.redis.info;

/**
 * _Redis 服务器的各种信息和统计数值
 */
public class RedisInfoEntry {
    // ==============================Fields===========================================
    private String name;
    private String value;
    private String description;

    // ==============================Methods==========================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
