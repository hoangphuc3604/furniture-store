package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.UserDTO;
import java.util.List;

public interface UserService {
    boolean changePassword(String username, String password);

    UserDTO getUserByUsername(String username);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    boolean updateUser(UserDTO userDTO);

    void deactivateUserById(Long id);

    void activateUserById(Long id);

    List<UserDTO> findAllNormalUser();
}
