package com.passwordnetwork.password_network.Services;

import java.util.List;

import com.passwordnetwork.password_network.DTO.PasswordResponseDTO;
import com.passwordnetwork.password_network.Models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public List<PasswordResponseDTO>StrongPasswordList(User user){
        List<PasswordResponseDTO> passwords = user.getStrongPasswords().stream()
        .map(p -> new PasswordResponseDTO( p.getPlataform(), p.getStrong_password()))
        .toList();
        return passwords;
    }


}
