package com.burikantushop.burikantushop.repository;

import com.burikantushop.burikantushop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom methods for user-related operations
    public List<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User findByUsernameOrEmail(String usernameOrEmail, String usernameOrEmail1);


}
