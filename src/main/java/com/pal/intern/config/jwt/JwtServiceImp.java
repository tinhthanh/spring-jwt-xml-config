package com.pal.intern.config.jwt;

import com.pal.intern.domain.User;
import com.pal.intern.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author tyler.intern
 */
@Service
@PropertySource(value = {"classpath:/jwt.properties"})
public class JwtServiceImp implements JwtService {

    private final Log LOGGER = LogFactory.getLog(this.getClass().getName());

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userService;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS384;

    public static final String AUDIENCE_UNKNOWN = "unknown";
    public static final String AUDIENCE_WEB = "web";
    public static final String AUDIENCE_MOBILE = "mobile";
    public static final String AUDIENCE_TABLET = "tablet";

    @Override
    public String getUserNameFromToKen(String token) {
        String userName;
        try {
            Claims claims = this.getAllClaimsFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            userName = null;
            LOGGER.error(e.getMessage());
        }
        return userName;
    }

    @Override
    public LocalDateTime getIssuedDate(String token) {
        LocalDateTime issueAt = null;
        try {
            Claims claims = this.getAllClaimsFromToken(token);
            issueAt = LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return issueAt;
    }

    @Override
    public String getToken(HttpServletRequest request) {

        /**
         * get token from from request header
         */
        String authRequest = request.getHeader(this.getRequestAuthHeader());
        if (authRequest != null) {
            if (authRequest.startsWith(this.getJwtRefix())) {
                /**
                 * subString token with (BEAR) length + space " "
                 */
                return authRequest.substring(this.getJwtRefix().length() + 1);
            }
        }

        /**
         * get token from AUTH cookie
         */
        Cookie cookie = this.getCookieByCookieName(this.getAuthCookieName(), request);
        if (cookie != null) {
            return cookie.getValue();
        }

        return null;

    }

    public Cookie getCookieByCookieName(String cookieName, HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;

    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = (Claims) Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(this.getJwtSecret())).parse(token).getBody();
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            claims = null;
        }
        return claims;
    }

    @Override
    public boolean validateToken(String authToken, UserDetails userDetails) {
        /**
         * check user name is exist on system user was login and store in
         * UserDetails
         */
        Optional<User> user = this.userService.getUserByUserName(userDetails.getUsername());
        if (!user.isPresent()) {
            return false;
        }
        String userNameFromToken = this.getUserNameFromToKen(authToken);
        LocalDateTime issueDate = this.getIssuedDate(authToken);

        LocalDateTime userLastPasswordResetDate = LocalDateTime.now();

        return (userNameFromToken != null && user.get().getUserName().equals(userNameFromToken) && (!isCreatedBeforeLastPasswordReset(issueDate, userLastPasswordResetDate)));
    }

    /**
     * check issue date of token is before last password reset date of user
     *
     * @param issueDate issue date of token
     * @param userLastPasswordResetDate last password reset date of user
     * @return boolean
     */
            private boolean isCreatedBeforeLastPasswordReset(LocalDateTime issueDate, LocalDateTime userLastPasswordResetDate) {
                return false;
        //        return issueDate.isBefore(userLastPasswordResetDate);
            }

    @Override
    public String generateToken(String username, Device device) {
        String token = null;
        try {
            Date dateIssued = new Date();
            token = Jwts.builder().setSubject(username)
                    .setAudience(this.getAudience(device))
                    .setIssuedAt(dateIssued)
                    .setExpiration(this.generateExpirationDate(device, dateIssued))
                    .signWith(SIGNATURE_ALGORITHM, DatatypeConverter.parseBase64Binary(this.getJwtSecret()))
                    .compact();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return token;
    }

    private Date generateExpirationDate(Device device, Date dateIssued) {
        long expiresIn = 0;
        try {
            /**
             * get expire time by device
             */
            expiresIn = this.getExpiredIn(device);
            
            LOGGER.info(expiresIn+" [expire time in second]");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new Date(dateIssued.getTime() + (expiresIn * 1000) );

    }

    private String getAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }

        return audience;

    }

    @Override
    public long getExpiredIn(Device device) {
        long timeExpiredIn = 0;

        if (device.isNormal()) {
            timeExpiredIn = this.getTimeExpirationOnNormalDevice();
        } else {
            if (device.isTablet() || device.isMobile()) {
                timeExpiredIn = this.getTimeExpirationOnMobileDevice();
            }
        }
        return timeExpiredIn;
    }

    @Override
    public String getAuthCookieName() {
        return this.environment.getRequiredProperty("jwt.cookie.name").trim();
    }

    @Override
    public String getAuthRefix() {
        return this.environment.getRequiredProperty("jwt.prefix").trim();
    }

    @Override
    public long getTimeExpirationOnNormalDevice() {
        return Long.valueOf(this.environment.getRequiredProperty("jwt.expiration.normal").trim());
    }

    @Override
    public long getTimeExpirationOnMobileDevice() {
        return Long.valueOf(this.environment.getRequiredProperty("jwt.expiration.mobile").trim());
    }

    @Override
    public String getJwtSecret() {
        return this.environment.getRequiredProperty("jwt.secret").trim();
    }

    @Override
    public String getRequestAuthHeader() {
        return this.environment.getRequiredProperty("jwt.request.header").trim();
    }

    @Override
    public String getJwtRefix() {
        return this.environment.getRequiredProperty("jwt.prefix").trim();
    }

    @Override
    public String getJwtIssuer() {
        return this.environment.getRequiredProperty("jwt.app.name").trim();
    }

}
