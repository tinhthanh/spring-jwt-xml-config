package com.pal.intern.repository;

import com.pal.intern.domain.Role;
import java.util.List;

/**
 *
 * @author tyler.intern
 */
public interface RoleRepostory {
    public List<Role>getListRolesByUserId(int userId);
}
