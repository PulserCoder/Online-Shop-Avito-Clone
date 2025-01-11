package ru.skypro.homework.dto.profile;

import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
}
