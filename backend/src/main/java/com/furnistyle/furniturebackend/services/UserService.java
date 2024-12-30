package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.UserDTO;
import com.furnistyle.furniturebackend.dtos.requests.UpdateUserRequest;
import java.util.List;

public interface UserService {
    boolean changePassword(String username, String olePassword, String newPassword);

    UserDTO getUserByToken(String token);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllAdmin();

    boolean updateUser(UpdateUserRequest updateUserRequest);

    void deactivateUserWithId(Long id);

    void activateUserWithId(Long id);

    List<UserDTO> getAllNormalUser();
}
