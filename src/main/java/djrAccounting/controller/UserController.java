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
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.listRoleByLoggedInUser());
        model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());

        return "user/user-create";
    }

    @PostMapping("/create")
    public String insertUser( @Valid @ModelAttribute("newUser") UserDto user, BindingResult bindingResult, Model model) {

        boolean isUsernameExist = userService.isUsernameExist(user);

            if(isUsernameExist){
                bindingResult.rejectValue("username", " ", "User already exist. Please try with different email");
            }
            if(bindingResult.hasErrors()) {
                return "/user/user_create";
            }
            /*
            model.addAttribute("newUser", user);
            model.addAttribute("userRoles", roleService.listRoleByLoggedInUser());
            model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());

            return "/user/user-create";
        }

             */

        userService.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("update/{userId}")
    public String editUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("roles", roleService.listRoleByLoggedInUser());
        model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());
        return "/user/user-update";
    }

    @PostMapping("/update/{userId}")
    public String updateUser(@PathVariable("userId") Long userId, @Valid @ModelAttribute("user") UserDto user,BindingResult bindingResult) {
       user.setId(userId);
       boolean emailExist = userService.isEmailExist(user);
        if (bindingResult.hasErrors() || emailExist) {
            if (emailExist) {
                bindingResult.rejectValue("username", " ", "User already exist. Please try with different username");
            }
            UserDto userDto = userService.findById(userId);
            user.setIsOnlyAdmin(userDto.getIsOnlyAdmin());
            userService.save(user);
            return "redirect:/users/list";
        }
        userService.update(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/users/list";
    }
}
