package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller

public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {

        this.userService = userService;
        this.roleRepository = roleRepository;
    }



    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String home(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", new User());
        return "admin/admin";
    }


    @PostMapping("/admin/add")
    public String addUser(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            @RequestParam(value = "rolesFromForm", required = false) List<String> rolesFromForm,
            Model model
    )
    {
        System.out.println(">>> POST /admin/add reached <<<");
        System.out.println("USER FROM FORM: username=" + user.getUsername()
                + ", name=" + user.getName()
                + ", lastName=" + user.getLastName()
                + ", age=" + user.getAge()
                + ", password=" + user.getPassword());
        System.out.println("ROLES FROM FORM: " + rolesFromForm);

        if (bindingResult.hasErrors()) {
            System.out.println(">>> VALIDATION ERRORS <<<");
            bindingResult.getFieldErrors().forEach(e ->
                    System.out.println(" - " + e.getField() + ": " + e.getDefaultMessage()
                            + " (rejected=" + e.getRejectedValue() + ")"));
            model.addAttribute("users", userService.getAllUsers());
            return "admin/admin";
        }

        if (rolesFromForm == null || rolesFromForm.isEmpty()) {
            rolesFromForm = List.of("ROLE_USER");
        }

        userService.addUser(user, rolesFromForm);
        return "redirect:/admin";
    }



    @GetMapping("/admin/edit")
    public String editUserForm(@RequestParam("id") int id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/edit";
    }

//    @PostMapping("/admin/update")
//    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "admin/edit";
//        }
//        userService.updateUser(user);
//        return "redirect:/admin";
//    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value="rolesFromForm", required=false) List<String> rolesFromForm,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "admin/edit";
        }

        userService.updateUser(user, rolesFromForm);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
