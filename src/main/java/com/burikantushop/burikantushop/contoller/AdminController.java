package com.burikantushop.burikantushop.contoller;

import com.burikantushop.burikantushop.model.User;
import com.burikantushop.burikantushop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/admin")
    public String showAdminPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Access denied. You are not an admin.");
                return "redirect:/";
            }
        } else {
            // User is not logged in, redirect to login page
            return "redirect:/login";
        }
    }
    @GetMapping("/dashboard")
    public String getAllUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }


    // Show form to edit a user
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.isAdmin()) {
                User user = userService.getUserById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
                model.addAttribute("user", user);
                return "edit_user"; // Thymeleaf template for editing user
            } else {
                model.addAttribute("error", "Access denied.");
                return "redirect:/";
            }
        } else {
            return "redirect:/login";
        }
    }

    // Handle update user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user, Model model) {
        try {
            userService.updateUser(id, user);
            return "redirect:/admin?success";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating user.");
            return "redirect:/admin?error";
        }
    }

    // Handle delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.isAdmin()) {
                userService.deleteUser(id);
                return "redirect:/admin?deleted";
            } else {
                model.addAttribute("error", "Access denied.");
                return "redirect:/";
            }
        } else {
            return "redirect:/login";
        }
    }

}
