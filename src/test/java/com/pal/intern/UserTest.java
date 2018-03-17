package com.pal.intern;

import com.pal.intern.repository.RoleRepostory;
import com.pal.intern.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author tyler.intern
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/app-config.xml"})
@WebAppConfiguration
public class UserTest {
    
    @Autowired
    UserService userService;
    @Autowired
    RoleRepostory roleRepostory;
    @Test
    public void test(){
      
    }
}
