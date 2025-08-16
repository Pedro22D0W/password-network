package com.passwordnetwork.password_network.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.passwordnetwork.password_network.Models.User;
import com.passwordnetwork.password_network.Repository.UserRepository;
import com.passwordnetwork.password_network.Services.UserService;
import com.passwordnetwork.password_network.infra.security.TokenService;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @RequestMapping("passwords")
    public ResponseEntity userPasswords(@RequestHeader("Authorization") String authorizationToken){
        String token = authorizationToken.replace("Bearer ", "");
        String userEmail = tokenService.ValidateToken(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok().body(userService.StrongPasswordList(user));
    }
}


