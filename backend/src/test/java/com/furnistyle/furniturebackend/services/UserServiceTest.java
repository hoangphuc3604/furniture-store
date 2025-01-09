package com.furnistyle.furniturebackend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.furnistyle.furniturebackend.dtos.bases.UserDTO;
import com.furnistyle.furniturebackend.dtos.requests.UpdateUserRequest;

import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.enums.EUserStatus;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.UserMapper;

import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.impl.UserServiceImpl;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Value("${application.default.password}")
    private String defaultPassword;

    @Test
    void changePassword_WhenUserNotFound_ThenThrowNotFoundException() {
        String username = "nonexistentUser";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> userService.changePassword(username, oldPassword, newPassword)
        );

        assertEquals(Constants.Message.INCORRECT_USERNAME_OR_PASS, exception.getMessage());
    }

    @Test
    void changePassword_WhenOldPasswordIncorrect_ThenThrowValidationException() {
        String username = "existingUser";
        String oldPassword = "wrongOldPass";
        String newPassword = "newPass";

        User user = Instancio.of(User.class)
            .set(Select.field(User::getUsername), username)
            .set(Select.field(User::getPassword), "encodedOldPass")
            .create();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.changePassword(username, oldPassword, newPassword)
        );

        assertEquals(Constants.Message.INCORRECT_OLD_PASS, exception.getMessage());
    }

    @Test
    void changePassword_WhenOldAndNewPasswordSame_ThenThrowBadRequestException() {
        String username = "existingUser";
        String oldPassword = "samePass";
        String newPassword = "samePass";

        User user = Instancio.of(User.class)
            .set(Select.field(User::getUsername), username)
            .set(Select.field(User::getPassword), "encodedOldPass")
            .create();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> userService.changePassword(username, oldPassword, newPassword)
        );

        assertEquals(Constants.Message.DUPLICATE_OLD_NEW_PASS, exception.getMessage());
    }

    @Test
    void changePassword_WhenValidRequest_ThenPasswordChangedSuccessfully() {
        String username = "existingUser";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        User user = Instancio.of(User.class)
            .set(Select.field(User::getUsername), username)
            .set(Select.field(User::getPassword), "encodedOldPass")
            .create();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPass");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        boolean result = userService.changePassword(username, oldPassword, newPassword);

        assertTrue(result);
        Mockito.verify(userRepository).save(user);
        assertEquals("encodedNewPass", user.getPassword());
    }

    // Test resetPassword
    @Test
    void resetPassword_WhenUserNotFound_ThenThrowNotFoundException() {
        String email = "nonexistent@example.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> userService.resetPassword(email)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void resetPassword_WhenValidEmail_ThenResetToDefaultPassword() {
        String email = "existing@example.com";
        User user = Instancio.of(User.class)
            .set(Select.field(User::getEmail), email)
            .create();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(defaultPassword)).thenReturn("encodedDefaultPass");

        userService.resetPassword(email);

        Mockito.verify(userRepository).save(user);
        assertEquals("encodedDefaultPass", user.getPassword());
    }

    // Test getUserByToken
    @Test
    void getUserByToken_WhenUserNotFound_ThenThrowNotFoundException() {
        String token = "validToken";
        String username = "nonexistentUser";

        Mockito.when(jwtService.extractUsername(token)).thenReturn(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> userService.getUserByToken(token)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void getUserByToken_WhenUserExists_ThenReturnUserDTO() {
        String token = "validToken";
        String username = "existingUser";

        User user = Instancio.of(User.class)
            .set(Select.field(User::getUsername), username)
            .create();

        UserDTO userDTO = Instancio.of(UserDTO.class).create();

        Mockito.when(jwtService.extractUsername(token)).thenReturn(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserByToken(token);

        assertEquals(userDTO, result);
    }

    @Test
    void getUserById_WhenUserNotFound_ThenThrowNotFoundException() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> userService.getUserById(userId)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void getUserById_WhenUserExists_ThenReturnUserDTO() {
        Long userId = 1L;
        User user = Instancio.create(User.class);
        UserDTO userDTO = Instancio.create(UserDTO.class);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertEquals(userDTO, result);
    }

    // Test getAllAdmin
    @Test
    void getAllAdmin_WhenAdminsExist_ThenReturnListOfUserDTOs() {
        List<User> adminUsers = Instancio.ofList(User.class).size(2).create();
        List<UserDTO> adminDTOs = Instancio.ofList(UserDTO.class).size(2).create();

        Mockito.when(userRepository.findAllByRole(ERole.ADMIN)).thenReturn(adminUsers);
        Mockito.when(userMapper.toDTOs(adminUsers)).thenReturn(adminDTOs);

        List<UserDTO> result = userService.getAllAdmin();

        assertEquals(adminDTOs, result);
    }

    @Test
    void getAllAdmin_WhenNoAdminsExist_ThenReturnEmptyList() {
        Mockito.when(userRepository.findAllByRole(ERole.ADMIN)).thenReturn(List.of());

        List<UserDTO> result = userService.getAllAdmin();

        assertTrue(result.isEmpty());
    }

    // Test updateUser
    @Test
    void updateUser_WhenUserNotFound_ThenThrowNotFoundException() {
        UpdateUserRequest updateUserRequest = Instancio.create(UpdateUserRequest.class);

        Mockito.when(userRepository.findById(updateUserRequest.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> userService.updateUser(updateUserRequest)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void updateUser_WhenValidRequest_ThenUpdateSuccessfully() {
        UpdateUserRequest updateUserRequest = Instancio.create(UpdateUserRequest.class);
        User user = Instancio.create(User.class);

        Mockito.when(userRepository.findById(updateUserRequest.getId())).thenReturn(Optional.of(user));

        boolean result = userService.updateUser(updateUserRequest);

        assertTrue(result);
        Mockito.verify(userRepository).save(user);
    }

    // Test deactivateUserWithId
    @Test
    void deactivateUserWithId_WhenUserNotFound_ThenThrowNotFoundException() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> userService.deactivateUserWithId(userId)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void deactivateUserWithId_WhenUserFound_ThenDeactivateUser() {
        Long userId = 1L;
        User user = Instancio.create(User.class);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deactivateUserWithId(userId);

        assertEquals(EUserStatus.INACTIVE, user.getStatus());
        Mockito.verify(userRepository).save(user);
    }

    // Test activateUserWithId
    @Test
    void activateUserWithId_WhenUserNotFound_ThenThrowNotFoundException() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> userService.activateUserWithId(userId)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void activateUserWithId_WhenUserFound_ThenActivateUser() {
        Long userId = 1L;
        User user = Instancio.create(User.class);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.activateUserWithId(userId);

        assertEquals(EUserStatus.ACTIVE, user.getStatus());
        Mockito.verify(userRepository).save(user);
    }

    // Test getAllNormalUser
    @Test
    void getAllNormalUser_WhenUsersExist_ThenReturnListOfUserDTOs() {
        List<User> normalUsers = Instancio.ofList(User.class).size(3).create();
        List<UserDTO> userDTOs = Instancio.ofList(UserDTO.class).size(3).create();

        Mockito.when(userRepository.findAllByRole(ERole.USER)).thenReturn(normalUsers);
        Mockito.when(userMapper.toDTOs(normalUsers)).thenReturn(userDTOs);

        List<UserDTO> result = userService.getAllNormalUser();

        assertEquals(userDTOs, result);
    }

    @Test
    void getAllNormalUser_WhenNoUsersExist_ThenReturnEmptyList() {
        Mockito.when(userRepository.findAllByRole(ERole.USER)).thenReturn(List.of());

        List<UserDTO> result = userService.getAllNormalUser();

        assertTrue(result.isEmpty());
    }
}
