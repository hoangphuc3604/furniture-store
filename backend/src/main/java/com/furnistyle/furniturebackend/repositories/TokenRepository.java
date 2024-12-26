package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
        SELECT t FROM Token t INNER JOIN User u\s
        ON t.user.id = u.id\s
        WHERE u.id = :id AND (t.expired = false or t.revoked = false)\s
        """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
