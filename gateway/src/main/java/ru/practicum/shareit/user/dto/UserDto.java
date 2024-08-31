package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.markers.Update;

@Data
@AllArgsConstructor
public class UserDto {
    @NotNull(groups = Update.class)
    private Long id;
    private String name;
    @NotBlank
    @Email
    private String email;
}
