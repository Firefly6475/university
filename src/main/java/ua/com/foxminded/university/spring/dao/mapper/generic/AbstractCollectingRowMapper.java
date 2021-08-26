package ua.com.foxminded.university.spring.dao.mapper.generic;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractCollectingRowMapper<T> implements CollectingRowMapper<T> {

    @Override
    public T mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        T result = mapRow(resultSet, null, rowNum);
        while (nextRow(resultSet) && isRelated(resultSet, result)) {
            result = mapRow(resultSet, result, rowNum++);
        }
        if (resultSet.isAfterLast()) {
            return result;
        }
        resultSet.previous();
        return result;
    }

    protected boolean nextRow(ResultSet rs) throws SQLException {
        return rs.next();
    }

    protected abstract T mapRow(ResultSet rs, T partialResult, int rowNum) throws SQLException;

    protected abstract boolean isRelated(ResultSet rs, T partialResult) throws SQLException;
}
