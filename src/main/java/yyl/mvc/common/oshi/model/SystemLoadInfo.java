package yyl.mvc.common.oshi.model;

import java.io.Serializable;

/**
 * 系统负载信息
 */
@SuppressWarnings("serial")
public class SystemLoadInfo implements Serializable {

    // ==============================Fields===========================================
    /** 1分钟之前到现在的负载 */
    private Double oneLoad;

    /** 5分钟之前到现在的负载 */
    private Double fiveLoad;

    /** 15分钟之前到现在的负载 */
    private Double fifteenLoad;

    // ==============================Methods==========================================
    /**
     * @return the oneLoad
     */
    public Double getOneLoad() {
        return oneLoad;
    }

    /**
     * @param oneLoad the oneLoad to set
     */
    public void setOneLoad(Double oneLoad) {
        this.oneLoad = oneLoad;
    }

    /**
     * @return the fiveLoad
     */
    public Double getFiveLoad() {
        return fiveLoad;
    }

    /**
     * @param fiveLoad the fiveLoad to set
     */
    public void setFiveLoad(Double fiveLoad) {
        this.fiveLoad = fiveLoad;
    }

    /**
     * @return the fifteenLoad
     */
    public Double getFifteenLoad() {
        return fifteenLoad;
    }

    /**
     * @param fifteenLoad the fifteenLoad to set
     */
    public void setFifteenLoad(Double fifteenLoad) {
        this.fifteenLoad = fifteenLoad;
    }
}
