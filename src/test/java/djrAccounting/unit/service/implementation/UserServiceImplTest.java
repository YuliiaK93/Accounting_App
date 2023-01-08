package djrAccounting.unit.service.implementation;

import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void findById_Test(){

        when(userRepository.findUserById(anyLong())).thenThrow(new NoSuchElementException("User Not Found"));

        Throwable throwable = assertThrows(NoSuchElementException.class, ()->userService.findUserById(anyLong()));
        verify(userRepository).findUserById(anyLong());

        }
         }


