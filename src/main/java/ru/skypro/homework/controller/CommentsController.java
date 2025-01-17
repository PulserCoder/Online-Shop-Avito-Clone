package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable int id) {

        Comments comments = commentService.getComments(id);
        return comments != null ? ResponseEntity.ok(comments) : ResponseEntity.notFound().build()   ;
    }


    @PostMapping("{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {

        Comment saveComment = commentService.addComment(comment, 0 ,id);

        return saveComment != null ? ResponseEntity.ok(saveComment) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentOwner(#commentId)")
    @DeleteMapping("{id}/comments/{commentId}")
    public void deleteComment(@PathVariable int id, @PathVariable int commentId) {
        commentService.delComment(commentId);
    }

    @PreAuthorize("@commentServiceImpl.isCommentOwner(#commentId)")
    @PatchMapping("{id}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable int id,
                                                 @PathVariable int commentId,
                                                 @RequestBody CreateOrUpdateComment comment) {

        Comment saveComment = commentService.addComment(comment, commentId ,id);

        return saveComment != null ? ResponseEntity.ok(saveComment) : ResponseEntity.notFound().build();
    }
}
