package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

public interface CommentService {
    Comment addComment(CreateOrUpdateComment comment, int commentId, int adId);

    Comments getComments(int adId);

    void delComment(int commentId);

    boolean isCommentOwner(int commentId);
}
