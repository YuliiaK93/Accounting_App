package djrAccounting.service;

import djrAccounting.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto findById(Long id);
    List<RoleDto> listRoles();
}
