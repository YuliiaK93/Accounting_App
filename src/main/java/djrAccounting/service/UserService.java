package djrAccounting.service;


import djrAccounting.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto findById(Long id);
}

