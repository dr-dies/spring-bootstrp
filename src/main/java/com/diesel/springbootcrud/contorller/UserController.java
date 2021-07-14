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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.HashSet;
import java.util.List;


@RestController
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

    @GetMapping("/admin")
    public ModelAndView index(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<User> users = userService.allUsers();
        modelAndView.addObject("users", users);

        return modelAndView;
    }
    @PostMapping("/admin/adduser")
    public ModelAndView addUser(ModelAndView modelAndView,
                                @ModelAttribute("userAdd") User user,
                                @RequestParam(name = "role") String role) {
        user.setRoles(userService.getRole(role));
        userService.add(user);
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }
    @GetMapping("/admin/edit/{id}")
    public ModelAndView details(ModelAndView modelAndView, @PathVariable(name = "id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            modelAndView.setViewName("redirect:/admin");
            return modelAndView;
        }
        modelAndView.addObject("user", user);
        modelAndView.setViewName("edit");
        return  modelAndView;
    }

    @RequestMapping(value = "/admin/edit/{id}", method = RequestMethod.PATCH)
    public ModelAndView edit(ModelAndView modelAndView,
                             @ModelAttribute("user") User user,
                             @RequestParam(name = "role") String role) {
        modelAndView.setViewName("redirect:/admin");
        if (user != null) {
            user.setRoles(userService.getRole(role));
            userService.update(user);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/admin/delete/{id}",method =RequestMethod.DELETE)
    @ResponseBody
    public ModelAndView deleteUser(ModelAndView modelAndView, @PathVariable Long id) {
        modelAndView.setViewName("redirect:/admin");
        userService.delete(userService.getById(id));
        return modelAndView;
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