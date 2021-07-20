package com.diesel.springbootcrud.contorller;


import com.diesel.springbootcrud.entity.User;
import com.diesel.springbootcrud.service.RoleServiceImpl;
import com.diesel.springbootcrud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
public class UserController {


    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @GetMapping(value = "/")
    public String getLoginPage() {
        return "redirect:login";
    }

    @GetMapping(value = "/user")
    public String user(Model model) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("currentUser", userServiceImpl.findUserByEmail(user.getUsername()));
        return "user";
    }
    @GetMapping("/admin")
    public String index(Model model) {
        List<User> users = userServiceImpl.allUsers();
        model.addAttribute("users", users);
        return "admin";
    }
    @PostMapping("/admin/adduser")
    public String addUser(@ModelAttribute("userAdd") User user,
                          @RequestParam(name = "role") String role) {
        user.setRoles(roleServiceImpl.getRole(role));
        userServiceImpl.add(user);

        return "redirect:/admin";
    }
    @RequestMapping(value = "/admin/edit/{id}", method = RequestMethod.PATCH)
    public String edit(@ModelAttribute("user") User user,
                       @RequestParam(name = "role") String role) {
        if (user != null) {
            user.setRoles(roleServiceImpl.getRole(role));
            userServiceImpl.update(user);
        }

        return "redirect:/admin";
    }
    @RequestMapping(value = "/admin/delete/{id}",method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        userServiceImpl.delete(userServiceImpl.getById(id));
        return "redirect:/admin";
    }


}