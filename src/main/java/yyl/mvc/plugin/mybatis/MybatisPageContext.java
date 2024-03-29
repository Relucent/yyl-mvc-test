package yyl.mvc.plugin.mybatis;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MybatisPageContext implements Serializable {

    private long offset = 0L;
    private long limit = 1L;
    private long total = -1L;
    private boolean count = false;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }
}
