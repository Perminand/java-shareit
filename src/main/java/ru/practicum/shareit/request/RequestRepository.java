package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterId(Long userId, Sort sort);

    List<ItemRequest> findAllNotInUser(Long user, PageRequest of, Sort sort);

}
