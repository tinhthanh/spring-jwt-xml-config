package com.pal.intern.domain;

import java.util.Set;

/**
 *
 * @author ttlang
 */
public class User {

    private int userId;
    private String userName;
    private String password;
    private String fullName;
    private int status;
    private Set<Role> permission;

    public User(int userId, String userName, String password, String fullName, int status, Set<Role> permission) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.status = status;
        this.permission = permission;
    }

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public int getStatus() {
        return status;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Role> getPermission() {
        return permission;
    }

    public void setPermission(Set<Role> permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", userName=" + userName + ", password=" + password + ", fullName=" + fullName + ", status=" + status + ", permission=" + permission + '}';
    }

}
