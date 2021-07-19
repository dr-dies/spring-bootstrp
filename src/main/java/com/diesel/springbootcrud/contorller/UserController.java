package com.diesel.springbootcrud.contorller;


import com.diesel.springbootcrud.entity.Role;
import com.diesel.springbootcrud.entity.User;
import com.diesel.springbootcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String getLoginPage() {
        return "redirect:login";
    }

    @GetMapping(value = "/user")
    public String user(Model model) {
        model.addAttribute("currentUser", getUserData());
        return "user";
    }
    @GetMapping("/admin")
    public String index(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = userService.allUsers();
        model.addAttribute("users", users);
        return "admin";
    }
    @PostMapping("/admin/adduser")
    public String addUser(@ModelAttribute("userAdd") User user,
                          @RequestParam(name = "role") String role) {
        user.setRoles(userService.getRole(role));
        userService.add(user);

        return "redirect:/admin";
    }
    @RequestMapping(value = "/admin/edit/{id}", method = RequestMethod.PATCH)
    public String edit(@ModelAttribute("user") User user,
                       @RequestParam(name = "role") String role) {
        if (user != null) {
            user.setRoles(userService.getRole(role));
            userService.update(user);
        }

        return "redirect:/admin";
    }
    @RequestMapping(value = "/admin/delete/{id}",method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        userService.delete(userService.getById(id));
        return "redirect:/admin";
    }

    private User getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            return userService.findUserByEmail(user.getUsername());
        }

        return null;
    }
}