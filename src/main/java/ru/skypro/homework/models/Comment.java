package ru.skypro.homework.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "comment")
@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue
    private int pk;

    private long createdAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "ad")
    private Ad ad;
}

