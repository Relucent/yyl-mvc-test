package yyl.mvc.common.oshi.model;

import java.io.Serializable;

/**
 * 磁盘存储信息
 */
@SuppressWarnings("serial")
public class DiskInfo implements Serializable {
    // ==============================Fields===========================================
    /** 文件系统名 */
    private String name;
    /** 挂载路径 */
    private String mount;
    /** 盘符类型 */
    private String type;
    /** 总大小 */
    private Long total;
    /** 使用量 */
    private Long used;
    /** 空闲量 */
    private Long free;
    /** 使用率(百分比) */
    private Double usePercent;

    // ==============================Methods==========================================
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mount
     */
    public String getMount() {
        return mount;
    }

    /**
     * @param mount the mount to set
     */
    public void setMount(String mount) {
        this.mount = mount;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

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
}
