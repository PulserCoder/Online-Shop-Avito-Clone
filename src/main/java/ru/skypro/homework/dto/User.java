package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String mail;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
}
