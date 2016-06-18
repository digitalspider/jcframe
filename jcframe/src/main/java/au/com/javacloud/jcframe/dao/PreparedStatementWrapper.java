package au.com.javacloud.jcframe.dao;

import java.sql.PreparedStatement;

/**
 * Created by david on 19/06/16.
 */
public class PreparedStatementWrapper {

    private PreparedStatement preparedStatement;
    private boolean insertStmt = false;
    private String sql;

    public PreparedStatementWrapper(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public boolean isInsertStmt() {
        return insertStmt;
    }

    public void setInsertStmt(boolean insertStmt) {
        this.insertStmt = insertStmt;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
