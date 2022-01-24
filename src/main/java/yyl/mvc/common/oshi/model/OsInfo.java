package yyl.mvc.common.oshi.model;

import java.io.Serializable;

/**
 * 操作系统信息
 */
@SuppressWarnings("serial")
public class OsInfo implements Serializable {

    // ==============================Fields===========================================
    /** 系统详细信息 */
    private String detail;

    // ==============================Methods==========================================
    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
