package fr.keemto.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    HttpServletRequest request;

    @RequestMapping(method = RequestMethod.GET)
    public void logout(HttpServletResponse response) throws ServletException, IOException {
        // Shiro Logout Implementation
        //Subject subject = SecurityUtils.getSubject();
        //subject.logout();
        //request.logout();
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath());
    }
}
