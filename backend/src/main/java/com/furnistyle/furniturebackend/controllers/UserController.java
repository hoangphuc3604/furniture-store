package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.bases.UserDTO;
import com.furnistyle.furniturebackend.dtos.requests.UpdateUserRequest;
import com.furnistyle.furniturebackend.services.UserService;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/getCurrentUser")
    public ResponseEntity<UserDTO> getByToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.getUserByToken(token));
    }

    @GetMapping("/admin/getById")
    public ResponseEntity<UserDTO> getById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/superAdmin/getAllAdmin")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok(userService.getAllAdmin());
    }

    @GetMapping("/admin/getAllNormalUser")
    public ResponseEntity<List<UserDTO>> getAllNormalUser() {
        return ResponseEntity.ok(userService.getAllNormalUser());
    }

    @GetMapping("/admin/activeUserWithId")
    public ResponseEntity<String> activeUserWithId(@RequestParam Long id) {
        userService.activateUserWithId(id);
        return ResponseEntity.ok(Constants.Message.UNLOCK_USER_WITH_ID + id.toString());
    }

    @GetMapping("/admin/deactivateUserWithId")
    public ResponseEntity<String> deactivateUserWithId(@RequestParam Long id) {
        userService.deactivateUserWithId(id);
        return ResponseEntity.ok(Constants.Message.BLOCK_USER_WITH_ID + id.toString());
    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        if (userService.updateUser(updateUserRequest)) {
            return ResponseEntity.ok(Constants.Message.UPDATE_USER_SUCCESSFUL);
        } else {
            return ResponseEntity.ok(Constants.Message.UPDATE_USER_FAILED);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request) {
        if (userService.changePassword(request.get("username"), request.get("old_password"),
            request.get("new_password"))) {
            return ResponseEntity.ok(Constants.Message.UPDATE_USER_SUCCESSFUL);
        } else {
            return ResponseEntity.ok(Constants.Message.UPDATE_USER_FAILED);
        }
    }
}
