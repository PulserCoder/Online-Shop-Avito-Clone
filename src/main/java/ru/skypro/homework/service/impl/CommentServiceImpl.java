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

/**
 * Реализация сервиса для работы с комментариями.
 * <p>
 * Этот сервис предоставляет функциональность для добавления, получения и удаления комментариев.
 * Также он включает проверку, является ли текущий пользователь владельцем комментария.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    /**
     * Добавляет новый комментарий к объявлению.
     * <p>
     * Метод сохраняет новый комментарий в базу данных и возвращает DTO с данными комментария.
     * Для получения текущего пользователя используется {@link SecurityContextHolder}.
     * </p>
     *
     * @param comment объект с данными комментария для создания или обновления
     * @param commentId id комментария, если он обновляется, иначе 0
     * @param adId id объявления, к которому добавляется комментарий
     * @return объект комментария, преобразованный в DTO
     */
    @Override
    public Comment addComment(CreateOrUpdateComment comment, int commentId, int adId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);

        CommentEntity commentEntity = commentMapper.toEntity(comment, commentId, userEntity, adId);

        return commentMapper.toComment(commentRepository.save(commentEntity));
    }

    /**
     * Получает все комментарии к указанному объявлению.
     * <p>
     * Метод извлекает список комментариев для объявления с указанным id из базы данных.
     * Если объявление не существует, возвращается {@code null}.
     * </p>
     *
     * @param adId id объявления, к которому нужно получить комментарии
     * @return объект с комментариями, преобразованный в DTO
     */
    @Override
    @Transactional
    public Comments getComments(int adId) {

        AdEntity ad = adRepository.findById(adId).orElse(null);
        if (ad == null) {
            return null;
        }

        return commentMapper.toCommentsDto(ad);
    }

    /**
     * Удаляет комментарий по его id.
     * <p>
     * Метод удаляет комментарий из базы данных. Если комментарий с таким id не найден, ничего не происходит.
     * </p>
     *
     * @param commentId id комментария, который нужно удалить
     */
    @Override
    public void delComment(int commentId){
        commentRepository.deleteById(commentId);
    }

    /**
     * Проверяет, является ли текущий пользователь владельцем комментария.
     * <p>
     * Метод проверяет, совпадает ли id текущего пользователя с id автора комментария.
     * </p>
     *
     * @param commentId id комментария для проверки
     * @return {@code true}, если текущий пользователь является владельцем комментария, иначе {@code false}
     */
    @Override
    public boolean isCommentOwner(int commentId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);
        return commentRepository.findById(commentId)
                .map(comment -> comment.getAuthor().getId() == userEntity.getId())
                .orElse(false);
    }
}
