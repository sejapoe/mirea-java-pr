package ru.sejapoe.javapr.mapper;

import org.mapstruct.*;
import ru.sejapoe.javapr.domain.PostEntity;
import ru.sejapoe.javapr.dto.posts.CreatePostDto;
import ru.sejapoe.javapr.dto.posts.PostDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class})
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "author.id", source = "authorId")
    PostEntity toEntity(CreatePostDto postDto);

    PostDto toDto(PostEntity postEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PostEntity partialUpdate(PostDto postDto, @MappingTarget PostEntity postEntity);
}