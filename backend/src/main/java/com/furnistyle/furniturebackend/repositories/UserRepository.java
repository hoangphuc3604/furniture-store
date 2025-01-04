package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    List<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findById(Long id);

    List<User> findAllByRole(ERole role);

    User findByEmail(String email);
}
