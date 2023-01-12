package djrAccounting.service;


import djrAccounting.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto findById(Long id);

    UserDto findByUsername(String username);

    void save(UserDto user);

    void deleteUserById(Long id);

    UserDto update(UserDto user);

    List<UserDto> findAllFilterForLoggedInUser();

    List<UserDto> getFilteredUsers() throws Exception;

    List<UserDto> listAllUsers();
    boolean isUsernameExist(UserDto userDto);

    String checkIfUserCanBeDeleted(Long id);
}

