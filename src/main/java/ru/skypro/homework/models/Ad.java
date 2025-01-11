package ru.skypro.homework.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "ad")
@Entity
@Getter
@Setter
public class Ad {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String description;
    private String image;
    private int price;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad")
    private List<CommentEntity> comments;
}
