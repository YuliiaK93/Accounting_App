package djrAccounting.controller;

import djrAccounting.dto.UserDto;
import djrAccounting.service.CompanyService;
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
    private final CompanyService companyService;

    public UserController(RoleService roleService, UserService userService, CompanyService companyService) {
        this.roleService = roleService;
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String listAllUsers(Model model) {
        model.addAttribute("users", userService.findAllFilterForLoggedInUser());
        return "user/user-list";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDto());
        //TODO will be implemented after security context @Yuliia
        model.addAttribute("roles", roleService.listRoles());
        model.addAttribute("companies", companyService.listAllCompanies());

        return "/user/user-create";
    }

    @PostMapping("/create")
    public String insertUser( @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model) {
        //TODO will be implemented after security context of companies, roles
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllFilterForLoggedInUser());
            model.addAttribute("roles", roleService.listRoles());
            model.addAttribute("companies", companyService.listAllCompanies());

            return "user/user-create";
        }

        userService.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/user-list";
    }

    @GetMapping("update/{id}")
    public String editUser(@PathVariable("id") Long userId, Model model) {
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("roles", roleService.findById(1L));
        model.addAttribute("users", userService.listAllUsers());
        return "/user/update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findById(1L));
        model.addAttribute("companies", companyService.listAllCompanies());
        return "/user/user_update";
    }
}
