package djrAccounting.unit.service.implementation;

import djrAccounting.TestDocumentInitializer;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.User;
import djrAccounting.enums.Role;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.SecurityService;
import djrAccounting.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTestMentor {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    SecurityService securityService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    @DisplayName("When_find_by_id_then_success")
    public void GIVEN_ID_WHEN_FIND_BY_ID_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getTestUserDto(Role.ROOT_USER);
        // When
        when(userRepository.findUserById(userDto.getId())).thenReturn(new User());
        //when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
        var user = userService.findUserById(userDto.getId());
        // Then
        assertThat(user.getCompany().getTitle().equals(userDto.getCompany().getTitle()));
    }

    @Test
    @DisplayName("When_find_by_user_name_then_success")
    public void GIVEN_USERNAME_WHEN_FIND_BY_USERNAME_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getTestUserDto(Role.ADMIN);
        // When
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(new User());
        //when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
        var user = userService.findByUsername(userDto.getUsername());
        // Then
        assertThat(user.getCompany().getTitle().equals(userDto.getCompany().getTitle()));
    }

    @Test
    @DisplayName("When_get_filtered_users_then_success")
    public void GIVEN_ROOT_USER_WHEN_GET_FILTERED_USERS_THEN_SUCCESS(){
        // Given
        UserDto adminUserDto = TestDocumentInitializer.getTestUserDto(Role.ADMIN);
        User adminUser = mapperUtil.convert(adminUserDto, new User());
        UserDto rootUser = TestDocumentInitializer.getTestUserDto(Role.ROOT_USER);
        // When
        doReturn(Arrays.asList(adminUser)).when(userRepository).findAllByRole_Description("Admin");
        doReturn(rootUser).when(securityService).getLoggedInUser();
        var users = userService.getFilteredUsers();
        // Then
        assertThat(users.size() > 0);
        assertThat(users.get(0).getRole().getDescription().equals("Admin"));
    }

    @Test
    @DisplayName("Given UserDto when save then success")
    public void GIVEN_USER_DTO_WHEN_SAVE_THEN_SUCCESS(){
        // Given
        String testPassword = "$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK";
        UserDto userDto = TestDocumentInitializer.getTestUserDto(Role.ADMIN);
        User user = mapperUtil.convert(userDto, new User());
        doReturn(testPassword).when(passwordEncoder).encode(anyString());
        // When
        var resultUser = userService.save(userDto);
        // Then
        assertThat(resultUser.getPassword().equals(testPassword));
    }

    @Test
    @DisplayName("Given UserDto when update then success")
    public void GIVEN_USER_DTO_WHEN_UPDATE_THEN_SUCCESS(){
        // Given
        String testPassword = "$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK";
        UserDto userDto = TestDocumentInitializer.getTestUserDto(Role.ADMIN);
        User user = mapperUtil.convert(userDto, new User());
        UserDto updateUserDto = TestDocumentInitializer.getTestUserDto(Role.MANAGER);
        User updateUser = mapperUtil.convert(updateUserDto, new User());
        // When
        doReturn(testPassword).when(passwordEncoder).encode(anyString());
        doReturn(user).when(userRepository).findUserById(anyLong());
        doReturn(updateUser).when(userRepository).save(any(User.class));

        var resultUser = userService.update(updateUserDto);
        // Then
        assertThat(resultUser.getRole().getDescription().equals("Manager"));

    }

    @Test
    @DisplayName("Given id when delete then success")
    public void GIVEN_ID_WHEN_DELETE_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getTestUserDto(Role.ADMIN);
        User user = mapperUtil.convert(userDto, new User());
        // When
        doReturn(user).when(userRepository).findUserById(anyLong());

        userService.deleteUserById(anyLong());
        // Then
        verify(userRepository).save(any(User.class));
    }

    /*
    @Test
    void delete_happyPath(){
        User user = TestDocumentInitializer.getTestUserDto(Role.ADMIN);
        user.setIsDeleted(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Throwable throwable = catchThrowable(() -> {
            userService.deleteUserById(user.getId());
        });
        assertTrue(user.getIsDeleted());
        assertNotEquals("test@test.com", user.getUsername());
        assertNull(throwable);
    }*/

    @Test
    @DisplayName("When_given_non_existing_id_then_fail")
    public void GIVEN_NON_EXISTING_ID_WHEN_FIND_BY_ID_THEN_FAIL(){
        when(userRepository.findUserById(anyLong())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(anyLong()));
    }
}
