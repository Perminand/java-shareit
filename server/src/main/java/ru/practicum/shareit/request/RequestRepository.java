package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterId(Long userId, Sort sort);

    //    List<ItemRequest> findAllNotInUserId(Long userId, Pageable pageable);
    List<ItemRequest> findByUser_IdNot(Long userId, Pageable pageable);


}
