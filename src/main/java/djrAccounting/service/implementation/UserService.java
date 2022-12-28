package djrAccounting.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;

public interface UserService {

    void deleteByUserName(String username);

    void delete(String username);

    UserDto findByUserName(String userName);


}
