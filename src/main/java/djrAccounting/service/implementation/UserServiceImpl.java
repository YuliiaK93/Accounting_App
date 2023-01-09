package djrAccounting.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.entity.Company;
import djrAccounting.entity.User;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.CompanyService;
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
        return mapperUtil.convert(userRepository.findById(id).orElseThrow(), UserDto.class);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return mapperUtil.convert(user, new UserDto());
    }


    @Override
    public void save(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user1 = mapperUtil.convert(userDto, new User());
        user1.setEnabled(true);
        userRepository.save(user1);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).get();
            user.setIsDeleted(true);
            user.setUsername(user.getUsername() + "-" + user.getId());
            userRepository.save(user);

        }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.findUserById(userDto.getId());
        User convertedUser = mapperUtil.convert(userDto, new User());
        convertedUser.setId(user.getId());
        convertedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        convertedUser.setEnabled(user.isEnabled());
        userRepository.save(convertedUser);
        return findUserById(userDto.getId());
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findUserById(id);
        UserDto dto =  mapperUtil.convert(user, new UserDto());
        dto.setIsOnlyAdmin(checkIfOnlyAdminForCompany(dto));
        return dto;
    }

    private List<UserDto> findAllOrderByCompanyAndRole() {
        List<UserDto> list = userRepository.findAllOrderByCompanyAndRole(false).stream().map(currentUser -> {
                    Boolean isOnlyAdmin = currentUser.getRole().getDescription().equals("Admin");
                    UserDto userDto = mapperUtil.convert(currentUser, new UserDto());
                    userDto.setIsOnlyAdmin(isOnlyAdmin);
                    return userDto;
                })
                .collect(Collectors.toList());
        return list;
    }

    public List<UserDto> findAllFilterForLoggedInUser() {
        UserDto loggedInUser = securityService.getLoggedInUser();
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
    public boolean isEmailExist(UserDto userDto) {

        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userDto.getUsername()));
        return user.filter(value -> !value.getId().equals(userDto.getId())).isPresent();
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
            return userRepository.findAllByRoleDescriptionOrderByCompanyTitle("Admin").stream()
                    .map(user -> mapperUtil.convert(user, new UserDto()))
                    .peek(userDto -> userDto.setIsOnlyAdmin(isOnlyAdmin(userDto)))
                    .collect(Collectors.toList());
        } else {

            Company company = mapperUtil.convert(companyService.listCompaniesByLoggedInUser(), new Company());

            return userRepository.findAllByCompanyOrderByRoleDescription(company).stream()
                    .map(user -> mapperUtil.convert(user, new UserDto()))
                    .peek(userDto -> userDto.setIsOnlyAdmin(isOnlyAdmin(userDto)))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean isUsernameExist(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null)
            return false;
        return !Objects.equals(userDto.getId(), user.getId());
    }

    private boolean isOnlyAdmin(UserDto userDto) {
        List<User> admin = userRepository.findAllByCompanyId(userDto.getId()).stream()
                .filter(user -> user.getRole().getDescription().equals("Admin"))
                .collect(Collectors.toList());
        return admin.size() == 1;
    }
}