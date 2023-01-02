package djrAccounting.service.implementation;

import djrAccounting.dto.RoleDto;
import djrAccounting.entity.Role;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.RoleRepository;
import djrAccounting.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapper;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapper) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;
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
}
