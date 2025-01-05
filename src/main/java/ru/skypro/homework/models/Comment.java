package ru.skypro.homework.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "comment")
@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue
    private int id;
}

