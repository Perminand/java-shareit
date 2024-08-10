package ru.practicum.shareit.item.mappers;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

import static ru.practicum.shareit.item.mappers.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mappers.UserMapper.toUserDto;

public class CommentMapper {

    public static Comment toCommentDb(CommentDto commentDto, User author, Item item) {
        return Comment.builder()
                .id(commentDto.getId())
                .author(author)
                .item(item)
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
        if (comment.getItem() != null) {
            ItemDto itemDto = toItemDto(comment.getItem());
            commentDto.setItem(itemDto);
        }
        if (comment.getAuthor() != null) {
            UserDto userDto = toUserDto(comment.getAuthor());
            commentDto.setAuthorName(userDto.getName());
        }
        return commentDto;
    }
}
