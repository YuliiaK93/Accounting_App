package djrAccounting.service;


import djrAccounting.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto findById(Long id);

    UserDto findByUsername(String username);

    UserDto save(UserDto user);

    void deleteUserById(Long id);

    UserDto update(UserDto user);

    List<UserDto> findAllFilterForLoggedInUser();

    boolean isEmailExist(UserDto userDto);

    List<UserDto> getFilteredUsers();

    List<UserDto> listAllUsers();
    boolean isUsernameExist(UserDto userDto);

    String checkIfUserCanBeDeleted(Long id);
}

