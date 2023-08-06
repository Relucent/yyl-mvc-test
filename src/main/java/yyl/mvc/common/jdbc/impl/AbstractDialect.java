package yyl.mvc.common.jdbc.impl;

import yyl.mvc.common.jdbc.Dialect;
import yyl.mvc.common.jdbc.parser.CountSqlHelper;

public abstract class AbstractDialect implements Dialect {
    @Override
    public String getCountSql(String sql) {
        return CountSqlHelper.getCountSql(sql);
    }
}
