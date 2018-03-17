

package com.pal.intern.config.jwt;

/**
 *
 * @author tyler.intern
 */
public class UserAuth {
    private String userName;
    private String password;

    public UserAuth() {
    }

    public UserAuth(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAuth{" + "userName=" + userName + ", password=" + password + '}';
    }
    
    

}
