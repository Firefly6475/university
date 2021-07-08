package ua.com.foxminded.university.spring.dao.mapper.generic;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CollectingRowMapper<T> extends RowMapper<T> {

}
