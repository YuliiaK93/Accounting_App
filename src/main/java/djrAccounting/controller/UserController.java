package djrAccounting.controller;

import djrAccounting.dto.UserDto;
import djrAccounting.service.RoleService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;

    private final SecurityService securityService;

    public UserController(RoleService roleService, UserService userService, SecurityService securityService) {
        this.roleService = roleService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String listAllUsers(Model model){

        model.addAttribute("users", userService.listAllUsers());

        return "user/user-list";
    }

    @GetMapping("/user-create")
    public String createUser(Model model){

        model.addAttribute("user", new UserDto());
        //TODO will be implemented after security context @Yuliia
        model.addAttribute("roles", roleService.findById(1L));
        model.addAttribute("users", userService.listAllUsers());

        return "/user-create";
    }

    @PostMapping("/user-create")
    public String insertUser(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findById(1L));
            model.addAttribute("users", userService.listAllUsers());

            return "/user-create";
        }

        userService.save(user);
        return "redirect:/user-create";



    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){

        userService.deleteUserById(id);
        return "redirect:/users/list";
    }


























}
