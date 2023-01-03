package djrAccounting.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.SecurityService;
import djrAccounting.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, PasswordEncoder passwordEncoder, @Lazy SecurityService securityService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
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
    public List<UserDto> listAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> mapperUtil.convert(user, new UserDto()))
                .sorted(Comparator.comparing((UserDto user) ->
                                user.getCompany().getTitle())
                        .thenComparing(userDto -> userDto.getRole().getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(UserDto userDto) {
        User user1 = mapperUtil.convert(userDto, new User());
        user1.setPassword(passwordEncoder.encode(user1.getPassword()));
        userRepository.save(user1);
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user == null){
            throw new NoSuchElementException("User was not found");
        }
        user.get().setIsDeleted(true);
        user.get().setUsername(user.get().getUsername() + "-" + user.get().getId());
        userRepository.save(user.get());
    }

    @Override
    public UserDto update(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getId());
        User convertedUser = mapperUtil.convert(userDto, new User());
        convertedUser.setId(user.get().getId());
        convertedUser.setPassword(user.get().getPassword());
        userRepository.save(convertedUser);
        return findById(userDto.getId());
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
                        .filter(user -> user.getCompany().equals(loggedInUser.getCompany()))
                        .collect(Collectors.toList());
            default:
                return findAllOrderByCompanyAndRole();
        }
    }
}