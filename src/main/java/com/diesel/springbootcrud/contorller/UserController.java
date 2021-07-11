package com.diesel.springbootcrud.contorller;


import com.diesel.springbootcrud.entity.Role;
import com.diesel.springbootcrud.entity.User;
import com.diesel.springbootcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import java.util.HashSet;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String getLoginPage() {
        return "redirect:login";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("currentUser", getUserData());
        return "login";
    }
    @GetMapping(value = "user")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("currentUser", userService.findUserByUserName(user.getUsername()));
        modelAndView.setViewName("user");
        return modelAndView;
    }

    @GetMapping("admin")
    public String index(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("users", users);

        return "admin";
    }
    @PostMapping("/admin/adduser")
    public String addUser(@RequestParam(name = "first_name", defaultValue = "---") String firstName,
                          @RequestParam(name = "last_name", defaultValue = "---") String lastName,
                          @RequestParam(name = "email", defaultValue = "---") String email,
                          @RequestParam(name = "username") String username,
                          @RequestParam(name = "password") String password,
                          @RequestParam(name = "role") String role) {

        List<Role> roles = userService.getRoles();
        if(role.equals(roles.get(0).getRole())) {
            roles.remove(1);
        } else {
            roles.remove(0);
        }
        User user = new User(firstName, lastName, email, username, password);
        user.setRoles(new HashSet<>(roles));
        userService.saveUser(user);

        return "redirect:/admin";
    }
    @GetMapping("/admin/edit/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);

        return "edit";
    }

    @PostMapping("/admin/saveuser")
    public String saveUser(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "first_name") String firstName,
                           @RequestParam(name = "last_name") String lastName,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "role") String role) {

        User user = userService.getById(id);
        List<Role> roles = userService.getRoles();
        if(role.equals(roles.get(0).getRole())) {
            roles.remove(1);
        } else {
            roles.remove(0);
        }
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setRoles(new HashSet<>(roles));
            userService.edit(user);
        }

        return "redirect:/admin";
    }

    @PostMapping("/admin/deleteuser")
    public String deleteUser(@RequestParam(name = "id") Long id) {

        userService.delete(userService.getById(id));

        return "redirect:/admin";
    }

    private User getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User) authentication.getPrincipal();
            return userService.findUserByUserName(secUser.getUsername());
        }

        return null;
    }


}