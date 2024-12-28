package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.UserDTO;
import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.enums.EUserStatus;
import com.furnistyle.furniturebackend.exceptions.DataAccessException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.UserMapper;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean changePassword(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException("Tên người dùng: " + username + " không tồn tại!");
        }
        user.get().setPassword(passwordEncoder.encode(password));
        try {
            userRepository.save(user.get());
        } catch (Exception e) {
            throw new DataAccessException("Không thể thay đổi mật khẩu người dùng!");
        }

        return true;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return userMapper.toDTO(userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin người dùng với username: " + username)));
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin người dùng với id: " + id)));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userMapper.toDTOs(userRepository.findAll());
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new NotFoundException("User not found"));
        user.setAddress(userDTO.getAddress());
        user.setFullname(user.getFullname());
        user.setPhone(userDTO.getPhone());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new DataAccessException("Không thể cập nhật thông tin người dùng!");
        }

        return true;
    }

    @Override
    public void deactivateUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(EUserStatus.INACTIVE);
            userRepository.save(user);
        } else {
            throw new NotFoundException("Không tìm thấy thông tin người dùng với id: " + id);
        }
    }

    @Override
    public void activateUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(EUserStatus.ACTIVE);
            userRepository.save(user);
        } else {
            throw new NotFoundException("Không tìm thấy thông tin người dùng với id: " + id);
        }
    }

    @Override
    public List<UserDTO> findAllNormalUser() {
        return userMapper.toDTOs(userRepository.findAllByRole(ERole.USER));
    }
}
