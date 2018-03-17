package com.pal.intern.repository.imp;

import com.pal.intern.domain.Role;
import com.pal.intern.mapper.RoleMapper;
import com.pal.intern.repository.RoleRepostory;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tyler.intern
 */
@Repository
public class RoleRepositoryImp implements RoleRepostory {

    private final Log LOGGER = LogFactory.getLog(this.getClass());
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Role> getListRolesByUserId(int userId) {
        List<Role> result = Collections.emptyList();
        String sql = "select r.* from test_permission p left join test_role r on p.role_id = r.role_id where p.user_id =? ";
        try {
            result = this.jdbcTemplate.query(sql, new Object[]{userId}, new RoleMapper());
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

}
