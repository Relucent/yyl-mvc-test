package yyl.mvc.common.jdbc.parser;

import java.util.concurrent.atomic.AtomicReference;

public class CountSqlHelper {

    private static final AtomicReference<CountSqlParser> COUNT_SQL_PARSER = new AtomicReference<>(new CountSqlSimpleParser());

    public static String getCountSql(String sql) {
        return COUNT_SQL_PARSER.get().getCountSql(sql);
    }

    public static void setParser(CountSqlParser parser) {
        COUNT_SQL_PARSER.set(parser);
    }
}
