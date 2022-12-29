package djrAccounting.service;


import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto findById(Long id);
    UserDto findByUsername(String username);
}

