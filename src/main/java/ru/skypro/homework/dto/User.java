package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String mail;
    private String firstName;
    private String lastName;
    private Role phone;
    private String role;
    private String image;
}
