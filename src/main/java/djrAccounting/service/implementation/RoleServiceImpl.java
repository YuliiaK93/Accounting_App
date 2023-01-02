package djrAccounting.service.implementation;

import djrAccounting.dto.RoleDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.Role;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.RoleRepository;
import djrAccounting.service.RoleService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapper;
    private final SecurityService securityService;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapper, SecurityService securityService) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.securityService = securityService;
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
        switch (loggedInUser.getRole().getDescription()) {
            case "Root User":
                return listRoles().stream()
                        .filter(role -> role.getDescription().equals("Admin"))
                        .collect(Collectors.toList());
            case "Admin":
                return listRoles().stream()
                        .filter(role -> !role.getDescription().equals("Admin")
                                && !role.getDescription().equals("Root User"))
                        .collect(Collectors.toList());
            default:
                return listRoles();
        }
    }
}