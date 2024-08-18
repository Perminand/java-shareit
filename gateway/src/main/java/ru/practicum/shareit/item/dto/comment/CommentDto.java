package ru.practicum.shareit.item.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.item.ItemDtoFull;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String authorName;
    private ItemDtoFull item;
    private String text;
    private LocalDateTime created;

    public CommentDto(String text) {
        this.text = text;
    }
}



