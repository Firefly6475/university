package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.spring.dao.CrudDao;
import ua.com.foxminded.university.spring.dao.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    protected final JdbcTemplate jdbcTemplate;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String findAllPagedQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    public AbstractCrudDaoImpl(JdbcTemplate jdbcTemplate, String saveQuery, String findByIdQuery,
                               String findAllQuery, String findAllPagedQuery, String updateQuery,
                               String deleteByIdQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.findAllPagedQuery = findAllPagedQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(E entity) {
        jdbcTemplate.update(saveQuery, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                insert(ps, entity);
            }
        });
    }

    @Override
    public void saveAll(List<E> entities) {
        jdbcTemplate.batchUpdate(saveQuery, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                insertAll(ps, i, entities);
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }

    @Override
    public Optional<E> findById(String id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(findByIdQuery, rowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<E> findAll() {
        return jdbcTemplate.query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement(findAllQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
        }, rowMapper());
    }

    @Override
    public List<E> findAll(Page page) {
        int amountOnPage = page.getAmountOnPage();
        int pageNumber = page.getPageNumber();
        return jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(findAllPagedQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                preparedStatement.setInt(1, amountOnPage);
                preparedStatement.setInt(2, (pageNumber - 1) * amountOnPage);
                return preparedStatement;
            }
        }, rowMapper());
    }

    @Override
    public void update(E entity) {
        jdbcTemplate.update(updateQuery, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                update(ps, entity);
            }
        });
    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update(deleteByIdQuery, id);
    }

    protected abstract void insert(PreparedStatement preparedStatement, E entity)
            throws SQLException;

    protected abstract void insertAll(PreparedStatement preparedStatement, int i, List<E> entities)
            throws SQLException;

    protected abstract void update(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract RowMapper<E> rowMapper();
}
