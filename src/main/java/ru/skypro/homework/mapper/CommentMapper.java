package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.models.CommentEntity;

@Mapper
public interface CommentMapper {


    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorImage", source = "author.image"),
            @Mapping(target = "authorFirstName", source = "author.firstName")
    })
    Comment toCommentEntity(CommentEntity commentEntity);

}
