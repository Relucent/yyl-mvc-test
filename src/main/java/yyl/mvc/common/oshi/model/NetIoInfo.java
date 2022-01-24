package yyl.mvc.common.oshi.model;

import java.io.Serializable;

/**
 * 网络带宽信息
 */
@SuppressWarnings("serial")
public class NetIoInfo implements Serializable {

    // ==============================Fields===========================================
    /** 每秒钟接收的数据包,rxpck/s */
    private Long rxpck;

    /** 每秒钟发送的数据包,txpck/s */
    private Long txpck;

    /** 每秒钟接收的KB数,rxkB/s */
    private Long rxbyt;

    /** 每秒钟发送的KB数,txkB/s */
    private Long txbyt;
    
    // ==============================Methods==========================================
    /**
     * @return the rxpck
     */
    public Long getRxpck() {
        return rxpck;
    }

    /**
     * @param rxpck the rxpck to set
     */
    public void setRxpck(Long rxpck) {
        this.rxpck = rxpck;
    }

    /**
     * @return the txpck
     */
    public Long getTxpck() {
        return txpck;
    }

    /**
     * @param txpck the txpck to set
     */
    public void setTxpck(Long txpck) {
        this.txpck = txpck;
    }

    /**
     * @return the rxbyt
     */
    public Long getRxbyt() {
        return rxbyt;
    }

    /**
     * @param rxbyt the rxbyt to set
     */
    public void setRxbyt(Long rxbyt) {
        this.rxbyt = rxbyt;
    }

    /**
     * @return the txbyt
     */
    public Long getTxbyt() {
        return txbyt;
    }

    /**
     * @param txbyt the txbyt to set
     */
    public void setTxbyt(Long txbyt) {
        this.txbyt = txbyt;
    }
}