package com.passwordnetwork.password_network.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="strong_passwords")
@Entity(name="password")
@Getter
@Setter
@NoArgsConstructor
public class Password {
    public Password(String plataform,String StrongPassword, User user) {
        this.plataform = plataform;
        this.strong_password = StrongPassword;
        this.user = user;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String plataform;
    private String strong_password;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
