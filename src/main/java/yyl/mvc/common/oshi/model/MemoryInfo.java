package yyl.mvc.common.oshi.model;

import java.io.Serializable;

/**
 * 内存信息
 */
@SuppressWarnings("serial")
public class MemoryInfo implements Serializable {

    // ==============================Fields===========================================
    /** 内存总计 */
    private Long total;
    /** 内存使用量 */
    private Long used;
    /** 内存空闲量 */
    private Long free;
    /** 内存使用百分比% */
    private Double usePercent;
    /** 交换区总计 */
    private Long swapTotal;
    /** 交换区使用量 */
    private Long swapUsed;
    /** 交换区空闲量 */
    private Long swapFree;
    /** 交换区使用百分比% */
    private Double swapUsePercent;

    // ==============================Methods==========================================
    /**
     * @return the total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * @return the used
     */
    public Long getUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(Long used) {
        this.used = used;
    }

    /**
     * @return the free
     */
    public Long getFree() {
        return free;
    }

    /**
     * @param free the free to set
     */
    public void setFree(Long free) {
        this.free = free;
    }

    /**
     * @return the usePercent
     */
    public Double getUsePercent() {
        return usePercent;
    }

    /**
     * @param usePercent the usePercent to set
     */
    public void setUsePercent(Double usePercent) {
        this.usePercent = usePercent;
    }

    /**
     * @return the swapTotal
     */
    public Long getSwapTotal() {
        return swapTotal;
    }

    /**
     * @param swapTotal the swapTotal to set
     */
    public void setSwapTotal(Long swapTotal) {
        this.swapTotal = swapTotal;
    }

    /**
     * @return the swapUsed
     */
    public Long getSwapUsed() {
        return swapUsed;
    }

    /**
     * @param swapUsed the swapUsed to set
     */
    public void setSwapUsed(Long swapUsed) {
        this.swapUsed = swapUsed;
    }

    /**
     * @return the swapFree
     */
    public Long getSwapFree() {
        return swapFree;
    }

    /**
     * @param swapFree the swapFree to set
     */
    public void setSwapFree(Long swapFree) {
        this.swapFree = swapFree;
    }

    /**
     * @return the swapUsePercent
     */
    public Double getSwapUsePercent() {
        return swapUsePercent;
    }

    /**
     * @param swapUsePercent the swapUsePercent to set
     */
    public void setSwapUsePercent(Double swapUsePercent) {
        this.swapUsePercent = swapUsePercent;
    }
}