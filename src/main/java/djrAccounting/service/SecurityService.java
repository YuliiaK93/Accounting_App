package djrAccounting.service;

import djrAccounting.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {

    UserDto getLoggedInUser();
}
