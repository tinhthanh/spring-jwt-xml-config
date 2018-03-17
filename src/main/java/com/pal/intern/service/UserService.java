package com.pal.intern.service;

import com.pal.intern.domain.User;
import java.util.Optional;

/**
 *
 * @author tyler.intern
 */
public interface UserService {

    public Optional<User> getUserByUserName(String userName);

    public Optional<User> getUserByUserId(int userId);

}
