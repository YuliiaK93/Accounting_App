package djrAccounting.service;


import djrAccounting.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDto findById(Long id);
    UserDto findByUsername(String username);
    List<UserDto> listAllUsers();
    void save(UserDto user);

    void deleteUserById(Long id);

    UserDto update(UserDto user);
}

