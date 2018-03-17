package com.pal.intern.mapper;

import com.pal.intern.domain.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author ttlang
 */
public class UserRowMapper implements RowMapper<User>{

    public static final String USER_ID_COL = "user_id";
    public static final String USER_NAME_COL = "user_name";
    public static final String USER_PASSWORD_COL = "password";
    public static final String USER_FULL_NAME_COL = "full_name";
    public static final String USER_STATUS_COL = "status";

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt(USER_ID_COL));
        user.setUserName(rs.getString(USER_NAME_COL));
        user.setPassword(rs.getString(USER_PASSWORD_COL));
        user.setFullName(rs.getNString(USER_FULL_NAME_COL));
        user.setStatus(rs.getInt(USER_STATUS_COL));
        return user;
    }

   
}
