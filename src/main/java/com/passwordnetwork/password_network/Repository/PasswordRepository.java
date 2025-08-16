package com.passwordnetwork.password_network.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passwordnetwork.password_network.Models.Password;

public interface PasswordRepository extends JpaRepository<Password, Long> {

}
