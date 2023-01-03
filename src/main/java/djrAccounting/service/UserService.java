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

    void update(UserDto user);

    List<UserDto> findAllFilterForLoggedInUser(UserDto user);

    boolean isEmailExist(UserDto userDto);

    List<UserDto> getFilteredUsers() throws Exception;

    List<UserDto> listAllUsers();
}

