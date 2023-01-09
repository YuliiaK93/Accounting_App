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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    MapperUtil mapperUtil;



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
    void findById_Test(long id){

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

         }


