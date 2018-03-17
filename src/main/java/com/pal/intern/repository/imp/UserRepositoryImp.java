package com.pal.intern.repository.imp;

import com.pal.intern.domain.User;
import com.pal.intern.mapper.UserRowMapper;
import com.pal.intern.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ttlang
 */
@Repository
public class UserRepositoryImp implements UserRepository {

    private final Log LOGGER = LogFactory.getLog(this.getClass().getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    @Override
    public Optional<User> getUserByUserName(String userName) {
        Optional<User> result = Optional.empty();
        String sql = "SELECT * FROM test_user u where u.user_name =?";
        try {
            User user = this.jdbcTemplate.queryForObject(sql,new Object[]{userName}, new UserRowMapper());
            result = Optional.ofNullable(user);
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<User> getUserByUserId(int userId) {
       Optional<User> result = Optional.empty();
        String sql = "SELECT * FROM test_user u where u.user_name =?";
        try {
            User user = this.jdbcTemplate.queryForObject(sql,new Object[]{userId}, new UserRowMapper());
            result = Optional.ofNullable(user);
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

}
