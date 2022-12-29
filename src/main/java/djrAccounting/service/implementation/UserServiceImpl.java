package djrAccounting.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapper;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.mapper = mapperUtil;
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return mapper.convert(user, new UserDto());
    }


    @Override
    public UserDto findByUserName(String username) {
        return mapper.convert(userRepository.findByUsername(username), new UserDto());
    }





    @Override
    public void save(UserDto userDto) {



    }
/*
    @Override
    public void deleteUser(String username) {

    }

*/


    @Override
    public List<UserDto> listAllUsers() {

        List<User> userList = userRepository.findAll();

        return userList.stream().map(user -> mapper.convert(user, new UserDto()))
                .collect(Collectors.toList());
    }


}
