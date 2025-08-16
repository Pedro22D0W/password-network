package com.passwordnetwork.password_network.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.passwordnetwork.password_network.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
}
