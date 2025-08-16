package com.passwordnetwork.password_network.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.passwordnetwork.password_network.DTO.PasswordDTO;
import com.passwordnetwork.password_network.Models.Password;
import com.passwordnetwork.password_network.Models.User;
import com.passwordnetwork.password_network.Repository.PasswordRepository;
import com.passwordnetwork.password_network.Repository.UserRepository;
import com.passwordnetwork.password_network.Services.PasswordStrengthService;
import com.passwordnetwork.password_network.infra.security.TokenService;

@RestController
@RequestMapping("/password")
public class PasswordController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private PasswordStrengthService passwordStrengthService;
    @PostMapping("/create")
    public ResponseEntity createStrongPassword(@RequestBody PasswordDTO weakPassword,@RequestHeader("Authorization") String authorizationToken){
        String token = authorizationToken.replace("Bearer ", "");
        String userEmail = tokenService.ValidateToken(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String strong_password = passwordStrengthService.strengthenPassword(weakPassword.password());
        Password strongPassword = new Password(weakPassword.plataform(),strong_password,user);
        passwordRepository.save(strongPassword);
        
        return ResponseEntity.ok().build();

    }

}
