package org.fakultet.vjezba3.controller;

import jakarta.servlet.http.HttpSession;
import org.fakultet.vjezba3.model.User;
import org.fakultet.vjezba3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Username already exists!");
                return "redirect:/users/register";
            }
            if (userService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email already exists!");
                return "redirect:/users/register";
            }
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/users/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        return userService.findByUsername(username)
            .map(user -> {
                if (user.getPassword().equals(password)) {
                    session.setAttribute("loggedInUser", user);
                    redirectAttributes.addFlashAttribute("success", "Login successful! Welcome " + user.getUsername());
                    return "redirect:/";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Invalid password!");
                    return "redirect:/users/login";
                }
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "User not found!");
                return "redirect:/users/login";
            });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("loggedInUser");
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully!");
        return "redirect:/";
    }

    @GetMapping("/new")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "User added successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add user: " + e.getMessage());
            return "redirect:/users/new";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return userService.findById(id)
            .map(user -> {
                model.addAttribute("user", user);
                return "user-edit";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "User not found!");
                return "redirect:/users";
            });
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, 
                            @ModelAttribute User user, 
                            RedirectAttributes redirectAttributes) {
        try {
            userService.update(id, user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
            return "redirect:/users/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}")
    public String showUserDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return userService.findById(id)
            .map(user -> {
                model.addAttribute("user", user);
                return "user-detail";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "User not found!");
                return "redirect:/users";
            });
    }
}
