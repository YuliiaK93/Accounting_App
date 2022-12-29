package djrAccounting.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public UserDto findById(Long id) {
        return mapperUtil.convert(userRepository.findById(id).orElseThrow(), UserDto.class);
    }

    @Override
    public UserDto findByUsername(String username) {
        return mapperUtil.convert(userRepository.findByUsername(username), new UserDto());
    }
}
