package djrAccounting.unit.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    MapperUtil mapperUtil;
    private UserDto user;


    @Test
    void findByUsername_Test() {

        userService.findByUsername("manager2@greentech.com");
        verify(userRepository).findByUsername("manager2@greentech.com");
        verify(userRepository, times(1)).findByUsername("manager2@greentech.com");
        verify(userRepository, atLeastOnce()).findByUsername("manager2@greentech.com");
        verify(userRepository, atMostOnce()).findByUsername("manager2@greentech.com");

        User user = userRepository.findByUsername("manager2@greentech.com");
        InOrder inOrder = inOrder(userRepository, mapperUtil);
        inOrder.verify(userRepository).findByUsername("manager2@greentech.com");
        inOrder.verify(mapperUtil).convert(user, new UserDto());

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void findById_Test(long id) {

        //Given
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mapperUtil.convert(user, UserDto.class)).thenReturn(new UserDto());

        //When
        userService.findById(id);

        //Then
        verify(userRepository).findById(id);
        verify(mapperUtil).convert(user, UserDto.class);

    }

    @Test
    public void save_Test() {  //NullPointerException
        given(userRepository.findById(1L)).willReturn(Optional.of(new User()));

        assertThrows(ConfigDataResourceNotFoundException.class, () -> {
            userService.save(new UserDto());
        });
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    public void update_Test() { //NullPointerException
        given(userRepository.save(new User())).willReturn(new User());

        user.setUsername("yulia.karnoza@gmail.com");
        user.setId(2L);

        UserDto updatedUser = userService.update(user);

        assertThat(updatedUser.getUsername()).isEqualTo("yulia.karnoza@gmail.com");
        assertThat(updatedUser.getId()).isEqualTo(2L);
    }

    @Test
    public void deleteUserById_Test() { //NoSuchElementException
        long userId = 1L;
        willDoNothing().given(userRepository).deleteById(userId);

        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

}


