package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.mapper.utils.CommentMappersUtils;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.CommentEntity;
import ru.skypro.homework.models.UserEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {CommentMappersUtils.class},
        imports = {LocalTime.class, Collectors.class})
public interface CommentMapper {


    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorImage", source = "author.image"),
            @Mapping(target = "authorFirstName", source = "author.firstName")
    })
    Comment toComment(CommentEntity commentEntity);

    @Mappings({
            @Mapping(target = "pk", source = "commentId"),
            @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())"),
            @Mapping(target = "author", source = "author"),
            @Mapping(target = "text", source = "dto.text"),
            @Mapping(target = "ad", qualifiedByName = "getAdById", source = "adId")
    })
    CommentEntity toEntity(CreateOrUpdateComment dto, int commentId, UserEntity author, int adId);

    List<Comment> toCommentDtoList(List<CommentEntity> commentEntities);

    @Mappings({
            @Mapping(target = "count", expression = "java((long) ad.getComments().size())"),
            @Mapping(target = "results", expression = "java(ad.getComments().stream().map(this::toComment).collect(Collectors.toList()))")
    })
    Comments toCommentsDto(AdEntity ad);



}
