package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;
import djrAccounting.enums.RoleEnum;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTestMentor {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    void update() {
        //Given
        UserDto userDto = TestConstants.getTestUserDto(RoleEnum.ADMIN);
        User user = mapperUtil.convert(userDto, new User());

        doReturn(TestConstants.PASSWORD_ENCODED_ABC1).when(passwordEncoder).encode(anyString());

        user.setFirstname(TestConstants.SAMPLE_FIRST_NAME_MRTZA);
        given(userRepository.save(any())).willReturn(user);

        //When
        var resultUser = userService.save(userDto);

        //Then
        assertEquals(TestConstants.SAMPLE_FIRST_NAME_MRTZA, resultUser.getFirstname());
    }
}