package yyl.mvc.common.queue;

import java.util.function.Function;

/**
 * 去除重复的元素
 */
public interface Distinct<T> {

    /**
     * 添加元素
     * @param element 元素
     * @return 如果元素已经存在则返回false,否则返回true.
     */
    boolean add(T element);

    /**
     * 移除元素
     * @param element 元素
     */
    void reomve(T element);

    /**
     * 清空元素
     */
    void clear();

    /** 元素摘要类 */
    @FunctionalInterface
    public interface DistinctDigester<T> extends Function<T, String> {}
}
