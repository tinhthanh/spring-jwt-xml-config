package com.pal.intern.rest;

import com.pal.intern.domain.User;
import com.pal.intern.repository.UserRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pal.intern.config.jwt.JwtService;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ttlang
 */
@RestController
public class HomeController {

    Log log = LogFactory.getLog(this.getClass().getName());

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity<List<User>> getHomePage(Device device, HttpServletRequest request) {
        String msg;
        if (device.isMobile()) {
            msg = "mobile" + device.getDevicePlatform();
        } else if (device.isTablet()) {
            msg = "tablet" + device.getDevicePlatform();
        } else {
            msg = "desktop" + device.getDevicePlatform();
        }
//        log.error(msg);
//        log.error(jwtService.getExpiredIn(device));
//        log.error(request.getHeader("User-Agent"));
//        log.warn(jwtService.generateToken("tyler.intern", device));
        String token  = this.jwtService.getToken(request);
        log.error(this.jwtService.getIssuedDate(token)+" issue date of token");
        log.error(this.jwtService.getAllClaimsFromToken(token).getExpiration() + " time expire");
        
        return new ResponseEntity("hi", HttpStatus.OK);
    }

}
