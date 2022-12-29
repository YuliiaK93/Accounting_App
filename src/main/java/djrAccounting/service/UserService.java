package djrAccounting.service;


import djrAccounting.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    UserDto findById(Long id);

    UserDto findByUserName(String username);

   // List<UserDto> listAllUsers();

   // void update(UserDto userDto);

    void save(UserDto user);

   // void deleteUser(String username);
}

