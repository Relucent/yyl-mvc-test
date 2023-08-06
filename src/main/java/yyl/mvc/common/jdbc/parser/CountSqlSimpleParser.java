package yyl.mvc.common.jdbc.parser;

import yyl.mvc.common.jdbc.SqlUtil;

public class CountSqlSimpleParser implements CountSqlParser {

    @Override
    public String getCountSql(String sql) {
        return "SELECT COUNT(*) AS COUNT___Y FROM (" + SqlUtil.removeOrderByExpression(sql) + ") T___Y ";
    }
}
