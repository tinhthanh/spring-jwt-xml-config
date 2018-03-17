package com.pal.intern.service.imp;

import com.pal.intern.domain.Role;
import com.pal.intern.domain.User;
import com.pal.intern.repository.RoleRepostory;
import com.pal.intern.repository.UserRepository;
import com.pal.intern.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tyler.intern
 */
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepostory roleRepostory;

    @Override
    public Optional<User> getUserByUserName(String userName) {
        Optional<User> user = this.userRepository.getUserByUserName(userName);
        if (user.isPresent()) {
            List<Role> userListRole = this.roleRepostory.getListRolesByUserId(user.get().getUserId());
            if (userListRole != null) {
                user.get().setPermission(new HashSet<>(userListRole));
            }
        }
        return user;
    }

    @Override
    public Optional<User> getUserByUserId(int userId) {
        Optional<User> user = this.userRepository.getUserByUserId(userId);
        if (user.isPresent()) {
            List<Role> userListRole = this.roleRepostory.getListRolesByUserId(user.get().getUserId());
            if (userListRole != null) {
                user.get().setPermission(new HashSet<>(userListRole));
            }
        }
        return user;
    }

}
