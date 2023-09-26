package yyl.mvc.plugin.hibernate.id;

import com.github.relucent.base.common.identifier.IdUtil;

/**
 * 基于SnowFlake的ID生成器
 */
public class SnowFlakeIdGenerator implements IdGenerator<Long> {

    /**
     * 获得唯一编码
     * @return 唯一编码
     */
    @Override
    public Long generateId() {
        return IdUtil.snowflakeId();
    }
}
