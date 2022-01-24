package yyl.mvc.common.oshi.model;

import java.io.Serializable;

/**
 * CPU相关信息
 */
@SuppressWarnings("serial")
public class CpuInfo implements Serializable {
    // ==============================Fields===========================================
    /** CPU型号信息 */
    private String model;
    /** CPU核心数(逻辑核心数) */
    private Integer cores;
    /** CPU系统使用率 */
    private Double sys;
    /** CPU用户使用率 */
    private Double used;
    /** CPU当前等待率 */
    private Double wait;
    /** CPU当前空闲率 */
    private Double free;

    // ==============================Methods==========================================
    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the cores
     */
    public Integer getCores() {
        return cores;
    }

    /**
     * @param cores the cores to set
     */
    public void setCores(Integer cores) {
        this.cores = cores;
    }

    /**
     * @return the sys
     */
    public Double getSys() {
        return sys;
    }

    /**
     * @param sys the sys to set
     */
    public void setSys(Double sys) {
        this.sys = sys;
    }

    /**
     * @return the used
     */
    public Double getUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(Double used) {
        this.used = used;
    }

    /**
     * @return the wait
     */
    public Double getWait() {
        return wait;
    }

    /**
     * @param wait the wait to set
     */
    public void setWait(Double wait) {
        this.wait = wait;
    }

    /**
     * @return the free
     */
    public Double getFree() {
        return free;
    }

    /**
     * @param free the free to set
     */
    public void setFree(Double free) {
        this.free = free;
    }
}
