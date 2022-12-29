package djrAccounting.controller;

import djrAccounting.dto.UserDto;
import djrAccounting.service.RoleService;
import djrAccounting.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createUser(Model model){

        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", roleService.findById(1L));
        model.addAttribute("users", userService.listAllUsers());

        return "/user/create";
    }

    @PostMapping("/create")
    public String insertUser(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findById(1L));
            model.addAttribute("users", userService.listAllUsers());

            return "/user/create";
        }

        userService.save(user);
        return "redirect:/user/create";



    }

























}
