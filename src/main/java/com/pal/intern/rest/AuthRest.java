package com.pal.intern.rest;

import com.pal.intern.config.api.ApiError;
import com.pal.intern.config.jwt.JwtService;
import com.pal.intern.config.jwt.UserAuth;
import com.pal.intern.config.jwt.UserTokenState;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tyler.intern
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthRest {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    private final Log LOGGER = LogFactory.getLog(this.getClass().getName());

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserAuth userAuth, HttpServletResponse response, Device device,HttpServletRequest request) {

        final UserDetails userDetails;
        LOGGER.info(userAuth);
        try {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userAuth.getUserName(),
                            userAuth.getPassword()));
            LOGGER.info(authenticationManager);
            LOGGER.info(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            userDetails = userDetailsService.loadUserByUsername(userAuth.getUserName());
            LOGGER.info(userDetails.getPassword() + " [get password from userdetail]");

        } catch (AuthenticationException e) {
            ApiError apiError = new ApiError();
            apiError.setStatus(HttpStatus.FORBIDDEN);
            apiError.setCode(HttpStatus.FORBIDDEN.value());
            apiError.setUrl(apiError.getFullURL(request));
            apiError.setErrors(Arrays.asList(e.getMessage()));
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        String jws = jwtService.generateToken(userDetails.getUsername(), device);
        int expiresIn = (int) jwtService.getExpiredIn(device);
        // Add cookie to response
        response.addCookie(createAuthCookie(jws, expiresIn));
        // Return the token

        UserTokenState userToken = new UserTokenState();
        userToken.setAccessToken(jws);
        userToken.setTokenType(this.jwtService.getJwtRefix());
        userToken.setExpireIn(expiresIn);
        return ResponseEntity.ok(userToken);
    }

    /**
     * create authentication cookie
     *
     * @param jwt json web token
     * @param expiresIn cookie expiration time
     * @return authentication cookie
     */
    private Cookie createAuthCookie(String jwt, int expiresIn) {
//        jwt = jwt.substring(jwtService.getAuthRefix().length());
        Cookie authCookie = new Cookie(jwtService.getAuthCookieName(), (jwt));
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(expiresIn);
        return authCookie;

    }
}
