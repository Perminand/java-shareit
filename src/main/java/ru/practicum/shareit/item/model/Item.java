package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "name", "description", "available"})
@EqualsAndHashCode(of = {"id", "name", "description", "available"})
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
