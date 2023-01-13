package djrAccounting.service.implementation;

import djrAccounting.dto.RoleDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.Company;
import djrAccounting.entity.Role;
import djrAccounting.entity.User;
import djrAccounting.exception.UserNotFoundException;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.CompanyService;
import djrAccounting.service.RoleService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    private final CompanyService companyService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, PasswordEncoder passwordEncoder, @Lazy SecurityService securityService, @Lazy CompanyService companyService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
        this.companyService = companyService;

    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapperUtil.convert(user, UserDto.class);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = mapperUtil.convert(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        return mapperUtil.convert(userRepository.save(user), UserDto.class);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setIsDeleted(true);
        user.setUsername(user.getUsername() + "-" + user.getId());
        userRepository.save(user);
    }

    public String checkIfUserCanBeDeleted(Long id) {
        UserDto loggedInUser = securityService.getLoggedInUser();
        Optional<User> userWillBeDeleted = userRepository.findUserNotDeleted(id);
        User userDeleted = new User();
        if (userWillBeDeleted.isPresent()) {
            userDeleted = userWillBeDeleted.get();
            if (userDeleted.getRole().getDescription().equals("Root User"))
                return "Root User cannot be deleted";
            if (!userDeleted.getRole().getDescription().equals("Admin") &&
                    loggedInUser.getRole().getDescription().equals("Root User"))
                return "As Root User you can only delete Admins";
            if (userDeleted.getRole().getDescription().equals("Admin") &&
                    !loggedInUser.getRole().getDescription().equals("Root User"))
                return "Only Root User can delete Admins.";
            if (!userDeleted.getCompany().getId().equals(loggedInUser.getCompany().getId()) &&
                    !loggedInUser.getRole().getDescription().equals("Root User"))
                return "As Admin, you can delete managers and employees only from your company";
        } else return "There is no User with this id " + id;

        return "";
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        userDto.setPassword(passwordEncoder.encode(user.getPassword()));
        userDto.setEnabled(user.isEnabled());
        return mapperUtil.convert(userRepository.save(mapperUtil.convert(userDto, new User())), UserDto.class);
    }

    public UserDto findUserById(Long id) {
        return mapperUtil.convert(userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("User was not found")), new UserDto());
    }

    private List<UserDto> findAllOrderByCompanyAndRole() {
        List<UserDto> list = userRepository.findAllOrderByCompanyAndRole(false).stream().map(currentUser -> {
            Role admin = new Role();
            admin.setId(2L);
            admin.setDescription("Admin");
            List<User> listCurrentAdmins = userRepository.findAllByCompanyAndRole(currentUser.getCompany(), admin);
            Boolean isOnlyAdmin = listCurrentAdmins.size() == 1;
            UserDto userDto = mapperUtil.convert(currentUser, new UserDto());
            userDto.setIsOnlyAdmin(isOnlyAdmin);
            return userDto;
        }).collect(Collectors.toList());
        return list;
    }

    public List<UserDto> findAllFilterForLoggedInUser() {
        UserDto loggedInUser = securityService.getLoggedInUser();
        List<User> userList = null;
        switch (loggedInUser.getRole().getDescription()) {
            case "Root User":
                return findAllOrderByCompanyAndRole().stream()
                        .filter(user -> user.getRole().getDescription().equals("Admin"))
                        .collect(Collectors.toList());
            case "Admin":
                return findAllOrderByCompanyAndRole().stream()
                        .filter(user -> user.getCompany().getId().equals(loggedInUser.getCompany().getId()))
                        .collect(Collectors.toList());
            default:
                return findAllOrderByCompanyAndRole();
        }
    }

    @Override
    public List<UserDto> getFilteredUsers() {

        List<User> userList;
        if (isCurrentUserRootUser()) {
            userList = userRepository.findAllByRole_Description("Admin");
        } else {
            userList = userRepository.findAllByCompany_Title(getCurrentUserCompanyTitle());
        }
        return userList.stream()
                .sorted(Comparator.comparing((User u) -> u.getCompany().getTitle()).thenComparing(u -> u.getRole().getDescription()))
                .map(entity -> {
                    UserDto dto = mapperUtil.convert(entity, new UserDto());
                    dto.setIsOnlyAdmin(checkIfOnlyAdminForCompany(dto));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private boolean checkIfOnlyAdminForCompany(UserDto userDto) {
        Company company = mapperUtil.convert(userDto.getCompany(), new Company());
        List<User> admins = userRepository.findAllByCompany_Title(company.getTitle().equals("Admin"));
        return userDto.getRole().getDescription().equals("Admin") && admins.size() == 1;
    }

    private Object getCurrentUserCompanyTitle() {
        return securityService.getLoggedInUser().getCompany().getTitle();
    }

    private boolean isCurrentUserRootUser() {
        User user = mapperUtil.convert(securityService.getLoggedInUser(), new User());
        if (user.getRole().getDescription().equals("Root User")) {
            return true;
        }
        return false;
    }

    public List<UserDto> listAllUsers() {
        if (securityService.getLoggedInUser().getRole().getDescription().equals("Root User")) {
            return userRepository.findAllByRoleDescriptionOrderByCompanyTitle("Admin")
                    .stream().map(user -> mapperUtil.convert(user, new UserDto()))
                    .peek(userDto -> userDto.setIsOnlyAdmin(isOnlyAdmin(userDto))).collect(Collectors.toList());
        } else {
            Company company = mapperUtil.convert(companyService.listCompaniesByLoggedInUser(), new Company());

            return userRepository.findAllByCompanyOrderByRoleDescription(company)
                    .stream().map(user -> mapperUtil.convert(user, new UserDto()))
                    .peek(userDto -> userDto.setIsOnlyAdmin(isOnlyAdmin(userDto))).collect(Collectors.toList());

        }
    }

    @Override
    public boolean isUsernameExist(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null) return false;
        return !Objects.equals(userDto.getId(), user.getId());
    }

    @Override
    public boolean isEmailExist(UserDto userDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userDto.getUsername()));
        return user.filter(value -> !value.getId().equals(userDto.getId())).isPresent();
    }

    private boolean isOnlyAdmin(UserDto userDto) {
        List<User> admin = userRepository.findAllByCompanyId(userDto.getId())
                .stream().filter(user -> user.getRole().getDescription().equals("Admin")).collect(Collectors.toList());
        return admin.size() == 1;
    }
}