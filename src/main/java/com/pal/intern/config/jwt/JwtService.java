package com.pal.intern.config.jwt;

import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author tyler.intern
 */
public interface JwtService {

    public String getUserNameFromToKen(String token);

    public LocalDateTime getIssuedDate(String token);

    /**
     * get token from AUTH cookie if cookie value null get token from AUTH request start with BEAR 
     * @param request HttpServletRequest
     * @return String token
     */
    public String getToken(HttpServletRequest request);

    public Claims getAllClaimsFromToken(String token);

    public boolean validateToken(String authToken, UserDetails userDetails);

    public String generateToken(String username, Device device);

    public long getExpiredIn(Device device);

    public String getAuthCookieName();

    public String getAuthRefix();

    /**
     * get json web token secret key
     *
     * @return String secret key
     */
    public String getJwtSecret();

    public long getTimeExpirationOnNormalDevice();

    public long getTimeExpirationOnMobileDevice();

    public String getRequestAuthHeader();

    public String getJwtRefix();

    public String getJwtIssuer();

}
