package djrAccounting.unit.service.implementation;

import djrAccounting.dto.RoleDto;
import djrAccounting.entity.Role;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.RoleRepository;
import djrAccounting.service.implementation.RoleServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTestMentor {

    @Mock
    RoleRepository roleRepository;
    @Mock
    MapperUtil mapperUtil;
    @InjectMocks
    RoleServiceImpl roleService;

    @ParameterizedTest
    void findById_Test(Long id){

        //Given
        Role role = new Role();

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));
        when(mapperUtil.convert(role, RoleDto.class)).thenReturn(new RoleDto());

        roleService.findById(id);

        verify(roleRepository.findById(id));
        verify(mapperUtil).convert(role, RoleDto.class);
    }

}