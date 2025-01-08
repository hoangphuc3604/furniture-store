package com.furnistyle.furniturebackend.services;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.furnistyle.furniturebackend.dtos.requests.RegisterRequest;
import com.furnistyle.furniturebackend.enums.EGender;
import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.enums.EUserStatus;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.impl.UserServiceImpl;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.validation.ValidationException;
import java.time.LocalDate;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static User generateFakeUser() {
        return Instancio.of(User.class)
            .set(field(User.class, "username"), "testUser")
            .set(field(User.class, "password"), "oldPassword")
            .set(field(User.class, "address"), "fakeAddress")
            .set(field(User.class, "fullname"), "Fake Fullname")
            .set(field(User.class, "phone"), "0123456789")
            .set(field(User.class, "email"), "fake.email@example.com")
            .set(field(User.class, "dateOfBirth"), LocalDate.of(1990, 1, 1))
            .set(field(User.class, "gender"), EGender.MALE)
            .set(field(User.class, "role"), ERole.USER)
            .set(field(User.class, "status"), EUserStatus.ACTIVE)
            .create();
    }

    public static RegisterRequest generateFakeRegisterRequest() {
        return Instancio.of(RegisterRequest.class)
            .set(field(RegisterRequest.class, "username"), "fakeUsername")
            .set(field(RegisterRequest.class, "password"), "fakePassword")
            .set(field(RegisterRequest.class, "address"), "fakeAddress")
            .set(field(RegisterRequest.class, "fullname"), "Fake Fullname")
            .set(field(RegisterRequest.class, "phone"), "0123456789")
            .set(field(RegisterRequest.class, "email"), "fake.email@example.com")
            .set(field(RegisterRequest.class, "dateOfBirth"), LocalDate.of(1990, 1, 1))
            .set(field(RegisterRequest.class, "gender"), "MALE")
            .set(field(RegisterRequest.class, "role"), "USER")
            .create();
    }

//    @Test
//    void changePassword_WhenCorrectOldPasswordAndNewPasswordAreProvided_ThenPasswordIsUpdated() {
//        // Mock data
//        User user = generateFakeUser();
//        when(userRepository.findByUsername("testUser")).thenReturn(user);
//
//        boolean result = userService.changePassword("testUser", "oldPassword", "newPassword");
//
//        // Assertions
//        assertTrue(result);
//        verify(userRepository, times(1)).save(user);
//        assertTrue(passwordEncoder.matches("newPassword", user.getPassword()));
//    }

    @Test
    void changePassword_WhenUsernameDoesNotExist_ThenThrowNotFoundException() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.changePassword("nonexistentUser", "oldPassword", "newPassword");
        });

        assertEquals(Constants.Message.INCORRECT_USERNAME_OR_PASS, exception.getMessage());
    }

    @Test
    void changePassword_WhenOldPasswordDoesNotMatch_ThenThrowValidationException() {
        User user = generateFakeUser();
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.changePassword("testUser", "wrongOldPassword", "newPassword");
        });

        assertEquals(Constants.Message.INCORRECT_OLD_PASS, exception.getMessage());
    }

//    @Test
//    void changePassword_WhenOldPasswordEqualsNewPassword_ThenThrowBadRequestException() {
//        User user = generateFakeUser();
//        when(userRepository.findByUsername("testUser")).thenReturn(user);
//
//        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
//            userService.changePassword("testUser", "oldPassword", "oldPassword");
//        });
//
//        assertEquals(Constants.Message.DUPLICATE_OLD_NEW_PASS, exception.getMessage());
//    }

}
