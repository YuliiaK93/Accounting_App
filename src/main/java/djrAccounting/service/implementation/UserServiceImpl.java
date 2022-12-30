package djrAccounting.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.entity.Company;
import djrAccounting.entity.User;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public void makeUserEnableByCompany(Company company) {

        List<User> userList = userRepository.findAllByCompany(company);
        userList.forEach(user -> user.setEnabled(true));
        userRepository.saveAll(userList);
    }

    @Override
    public void makeUserDisableByCompany(Company company) {

        List<User> userList = userRepository.findAllByCompany(company);
        userList.forEach(user -> user.setEnabled(false));
        userRepository.saveAll(userList);
    }

    @Override
    public UserDto findByUsername(String username) {
        return mapperUtil.convert(userRepository.findByUsername(username), UserDto.class);
    }
}
