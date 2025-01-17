package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.CommentEntity;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    @Override
    public Comment addComment(CreateOrUpdateComment comment, int commentId, int adId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);


        CommentEntity commentEntity = commentMapper.toEntity(comment, commentId, userEntity, adId);

        return commentMapper.toComment(commentRepository.save(commentEntity));
    }

    @Override
    @Transactional
    public Comments getComments(int adId) {

        AdEntity ad = adRepository.findById(adId).orElse(null);
        if (ad == null) {
            return null;
        }

        return commentMapper.toCommentsDto(ad);
    }

    @Override
    public void delComment(int commentId){
        commentRepository.deleteById(commentId);
    }

    @Override
    public boolean isCommentOwner(int commentId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);
        return commentRepository.findById(commentId)
                .map(comment -> comment.getAuthor().getId() == userEntity.getId())
                .orElse(false);
    }

}
