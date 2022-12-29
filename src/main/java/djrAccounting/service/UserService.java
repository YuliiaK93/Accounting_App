package djrAccounting.service;


import djrAccounting.dto.UserDto;
import djrAccounting.entity.Company;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto findById(Long id);

    void makeUserEnableByCompany(Company company);
    void makeUserDisableByCompany(Company company);
}

