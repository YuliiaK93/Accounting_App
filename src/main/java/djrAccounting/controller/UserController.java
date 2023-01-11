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
    private final SecurityService securityService;

    public UserController(RoleService roleService, UserService userService, CompanyService companyService, SecurityService securityService) {
        this.roleService = roleService;
        this.userService = userService;
        this.companyService = companyService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String listAllUsers(Model model) {
        model.addAttribute("users", userService.findAllFilterForLoggedInUser());
        return "user/user-list";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.listRoleByLoggedInUser());
        model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());

        return "/user/user-create";
    }

    @PostMapping("/create")
    public String insertUser(@Valid @ModelAttribute("newUser") UserDto user, BindingResult bindingResult, Model model) {

        boolean isUsernameExist = userService.isUsernameExist(user);

        model.addAttribute("userRoles", roleService.listRoleByLoggedInUser());
        model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());
        if (isUsernameExist) {
            bindingResult.rejectValue("username", " ", "User already exist. Please try with different email");
        }
        if (bindingResult.hasErrors()) {
            return "/user/user-create";
        }

        userService.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {

        UserDto userDto = userService.findById(id);
        if(!userDto.getCompany().equals(securityService.getLoggedInUser().getCompany()))
        return "redirect:/users/list";
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("userRoles", roleService.listRoleByLoggedInUser());
        model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());
        return "/user/user-update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model) {

        boolean isUsernameExist = userService.isUsernameExist(user);
        model.addAttribute("userRoles", roleService.listRoleByLoggedInUser());
        model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());

        if (isUsernameExist) {
            bindingResult.rejectValue("username", " ", "User already exist. Please try with different username");
            return "/user/user-update";
        }
        if (bindingResult.hasErrors()) {
            return "/user/user-update";
        }
        userService.update(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        String error = userService.checkIfUserCanBeDeleted(id);
        if(!error.isBlank()) {
            model.addAttribute("users", userService.findAllFilterForLoggedInUser());
            model.addAttribute("error", error);
            return "/user/user-list";
        }
        userService.deleteUserById(id);
        return "redirect:/users/list";
    }
}
