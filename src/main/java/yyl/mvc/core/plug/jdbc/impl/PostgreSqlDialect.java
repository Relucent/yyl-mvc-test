package yyl.mvc.core.plug.jdbc.impl;

import yyl.mvc.core.plug.jdbc.Dialect;

/**
 * JDBC查询方言mysql实现，主要用于提供分页查询<br>
 */
public class PostgreSqlDialect implements Dialect {

    public static final PostgreSqlDialect INSTANCE = new PostgreSqlDialect();

    @Override
    public String getCountSql(String sql) {
        return "select count(*) as COUNT___y from (" + sql + ") T___T";
    }

    @Override
    public String getLimitSql(String sql, int start, int limit) {
        return sql + " limit " + limit + " offset " + start;
    }

    @Override
    public String testQuery() {
        return "select version()";
    }
}
