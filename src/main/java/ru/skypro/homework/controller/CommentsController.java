package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentsController {

    @GetMapping("{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable int id) {
        Comment comment = new Comment();

        comment.setPk(1);
        comment.setAuthor(1);
        comment.setText("This is a comment");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAuthorImage("path");

        List list = List.of(comment);

        Comments comments = new Comments();

        comments.setComments(list);
        comments.setCount(1);

        return ResponseEntity.ok(comments);
    }


    @PostMapping("{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        Comment comment1 = new Comment();
        comment1.setPk(id);
        comment1.setAuthor(1);
        comment1.setText(comment.getText());
        comment1.setCreatedAt(System.currentTimeMillis());
        comment1.setAuthorImage("path");

        return ResponseEntity.ok(comment1);
    }

    @DeleteMapping("{id}/comments/{commentId}")
    public void deleteComment(@PathVariable int id, @PathVariable int commentId) {

    }

    @PatchMapping("{id}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        Comment comment1 = new Comment();
        comment1.setPk(id);
        comment1.setAuthor(1);
        comment1.setText(comment.getText());
        comment1.setCreatedAt(System.currentTimeMillis());
        comment1.setAuthorImage("path");

        return ResponseEntity.ok(comment1);
    }
}
