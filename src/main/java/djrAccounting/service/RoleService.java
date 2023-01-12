package djrAccounting.service;

import djrAccounting.dto.RoleDto;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface RoleService {
    RoleDto findById(Long id) throws RoleNotFoundException;
    List<RoleDto> listRoles();
    List<RoleDto> listRoleByLoggedInUser();
}
