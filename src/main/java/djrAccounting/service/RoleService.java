package djrAccounting.service;

import djrAccounting.dto.RoleDto;
import djrAccounting.exception.RoleNotFoundException;

import java.util.List;

public interface RoleService {
    RoleDto findById(Long id) throws RoleNotFoundException;
    List<RoleDto> listRoles();
    List<RoleDto> listRoleByLoggedInUser();
}
