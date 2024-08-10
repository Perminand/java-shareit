package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(of = {"id", "description", "created"})
@EqualsAndHashCode(of = {"id", "description", "created"})
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "created_date")
    private LocalDateTime created;
}
