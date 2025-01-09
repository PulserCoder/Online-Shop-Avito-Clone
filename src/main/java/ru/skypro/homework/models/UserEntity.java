package ru.skypro.homework.models;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Table(name = "users")
@Entity
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue
    private long id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String image;
}
