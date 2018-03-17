package com.pal.intern.repository;

import com.pal.intern.domain.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ttlang
 */
public interface UserRepository {


    public Optional<User> getUserByUserName(String userName);

    public Optional<User> getUserByUserId(int userId);
}
