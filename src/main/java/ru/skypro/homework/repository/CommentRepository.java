package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
}
