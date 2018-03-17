package com.pal.intern.mapper;

import com.pal.intern.domain.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author ttlang
 */
public class RoleMapper implements RowMapper<Role> {

    public static final String ROLE_ID_COL = "role_id";
    public static final String ROLE_NAME_COL = "role_name";

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt(ROLE_ID_COL));
        role.setRoleName(rs.getString(ROLE_NAME_COL));
        return role;
    }

}
