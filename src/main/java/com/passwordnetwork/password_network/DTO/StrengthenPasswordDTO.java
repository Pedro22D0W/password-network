package com.passwordnetwork.password_network.DTO;

public record StrengthenPasswordDTO(String password,int minLength,boolean requireUppercase,boolean requireNumber,boolean requireSpecialChar) {

}
