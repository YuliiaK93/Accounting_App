package djrAccounting.service.implementation;

import djrAccounting.dto.RoleDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.Role;
import djrAccounting.entity.User;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.RoleRepository;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.RoleService;
import djrAccounting.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapper;
    private final SecurityService securityService;
    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapper, SecurityService securityService, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.securityService = securityService;
        this.userRepository = userRepository;
    }

    @Override
    public RoleDto findById(Long id) {
        return mapper.convert(roleRepository.findById(id).orElseThrow(), RoleDto.class);
    }

    @Override
    public List<RoleDto> listRoles() {
        List<Role> roleList = roleRepository.findAll();
        return roleList.stream().map(role -> mapper.convert(role, new RoleDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDto> listRoleByLoggedInUser() {
        UserDto loggedInUser = securityService.getLoggedInUser();
        List<Role> roleslist = roleRepository.findAll();
        if (loggedInUser.getRole().getDescription().equals("Root User")) {
            List<RoleDto> roleDtoList = roleslist.stream()
                    .filter(role -> role.getDescription().equals("Admin"))
                    .map(role -> mapper.convert(role, new RoleDto()))
                    .collect(Collectors.toList());
            return roleDtoList;
        } else if (loggedInUser.getRole().getDescription().equals("Admin")) {
            List<User> userList = userRepository.findAllByCompanyId(loggedInUser.getCompany().getId());
            log.info("User Roles size " + roleslist.size());
            return listRoles().stream()
                    .filter(role -> !role.getDescription().equals("Root User"))
                    .map(role -> mapper.convert(role, new RoleDto()))
                    .collect(Collectors.toList());
        }
            return Collections.emptyList();

    }
}