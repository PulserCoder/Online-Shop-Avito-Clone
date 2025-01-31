package ru.skypro.homework.dto.comment;

import lombok.Data;

import java.util.List;

@Data
public class Comments {

    private long count;

    private List<Comment> results;
}
