package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Comments", description = "API для управления комментариями")
public class CommentsController {

    private final CommentService commentService;

    @Operation(summary = "Получить все комментарии для объявления", description = "Возвращает список комментариев для объявления по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен"),
            @ApiResponse(responseCode = "404", description = "Объявление с указанным ID не найдено")
    })
    @GetMapping("{id}/comments")
    public ResponseEntity<Comments> getComments(
            @Parameter(description = "ID объявления, для которого нужно получить комментарии", example = "1")
            @PathVariable int id) {

        Comments comments = commentService.getComments(id);
        return comments != null ? ResponseEntity.ok(comments) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Добавить комментарий к объявлению", description = "Добавляет новый комментарий для указанного объявления.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Объявление с указанным ID не найдено")
    })
    @PostMapping("{id}/comments")
    public ResponseEntity<Comment> addComment(
            @Parameter(description = "ID объявления, к которому нужно добавить комментарий", example = "1")
            @PathVariable int id,
            @RequestBody CreateOrUpdateComment comment) {

        Comment saveComment = commentService.addComment(comment, 0, id);
        return saveComment != null ? ResponseEntity.ok(saveComment) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Удалить комментарий", description = "Удаляет комментарий по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Комментарий успешно удалён"),
            @ApiResponse(responseCode = "403", description = "Нет прав для удаления комментария"),
            @ApiResponse(responseCode = "404", description = "Комментарий с указанным ID не найден")
    })
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentOwner(#commentId)")
    @DeleteMapping("{id}/comments/{commentId}")
    public void deleteComment(
            @Parameter(description = "ID объявления, к которому относится комментарий", example = "1")
            @PathVariable int id,
            @Parameter(description = "ID комментария, который нужно удалить", example = "1")
            @PathVariable int commentId) {
        commentService.delComment(commentId);
    }

    @Operation(summary = "Обновить комментарий", description = "Обновляет комментарий с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлён"),
            @ApiResponse(responseCode = "403", description = "Нет прав для обновления комментария"),
            @ApiResponse(responseCode = "404", description = "Комментарий с указанным ID не найден")
    })
    @PreAuthorize("@commentServiceImpl.isCommentOwner(#commentId)")
    @PatchMapping("{id}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @Parameter(description = "ID объявления, к которому относится комментарий", example = "1")
            @PathVariable int id,
            @Parameter(description = "ID комментария, который нужно обновить", example = "1")
            @PathVariable int commentId,
            @RequestBody CreateOrUpdateComment comment) {

        Comment saveComment = commentService.addComment(comment, commentId, id);
        return saveComment != null ? ResponseEntity.ok(saveComment) : ResponseEntity.notFound().build();
    }
}
