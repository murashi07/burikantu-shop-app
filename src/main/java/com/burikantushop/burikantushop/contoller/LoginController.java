package com.burikantushop.burikantushop.contoller;

import com.burikantushop.burikantushop.model.User;
import com.burikantushop.burikantushop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request, HttpSession session) {
        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");

        User user = userService.authenticateUser(usernameOrEmail, password);

        if (user != null) {
            session.setAttribute("user", user);
            // Authentication successful, redirect to appropriate page
            if (user.isAdmin()) {
                System.out.println("Redirecting to admin page for user: " + user.getUsername());
                return new ModelAndView("redirect:/admin");
            } else {
                System.out.println("Redirecting to Home2 page for user: " + user.getUsername());
//                session.setAttribute("user",);
                return new ModelAndView("redirect:/api/user/profile");
            }
        } else {
            // Authentication failed, display error message
            System.out.println("Authentication failed for username/email: " + usernameOrEmail);
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("error", "Invalid username/email or password");
            return mav;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
