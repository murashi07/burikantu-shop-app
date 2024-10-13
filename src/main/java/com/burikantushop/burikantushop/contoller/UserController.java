package com.burikantushop.burikantushop.contoller;

import com.burikantushop.burikantushop.model.User;
import com.burikantushop.burikantushop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);

    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/registerUser")
    public String showUserRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user") User user) {
        user.setIsAdmin(false); // Set isAdmin to false for user registration
        userService.createUser(user);
        return "redirect:/";
    }

    // Display user profile
//    @GetMapping("/profile")
//    public String showUserProfile(HttpServletRequest request, Model model) {
//        HttpSession session = request.getSession();
//        if (session.getAttribute("user") != null) {
//            User user = (User) session.getAttribute("user");
//            model.addAttribute("user", user);
//            return "userProfile"; // Thymeleaf template for user profile
//        } else {
//            return "redirect:/login";
//        }
//    }

    // Display user profile with the ability to update
    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null && user.getId().equals(id)) {
            model.addAttribute("user", user);
            return "userProfile"; // Thymeleaf template for user profile
        } else {
            return "redirect:/login";
        }
    }

    // Update user profile
    @PutMapping("/profile/{id}")
    public String updateUserProfile(@PathVariable Long id, @ModelAttribute("user") User updatedUser, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser != null && currentUser.getId().equals(id)) {
            userService.updateUser(id, updatedUser); // Update the user
            session.setAttribute("user", updatedUser); // Update session with new user details
            return "redirect:/api/user/profile/" + id; // Redirect to updated profile
        } else {
            return "redirect:/login";
        }
    }

}

