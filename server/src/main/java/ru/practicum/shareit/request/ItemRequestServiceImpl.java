package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoLite;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDtoOut create(Long userId, ItemRequestDto itemRequestDto) {
        User user = getUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = requestRepository.save(itemRequest);
        return ItemRequestMapper.toRequestDtoOut(itemRequest);
    }

    @Override
    public List<ItemRequestDtoLite> findByUserId(Long userId) {
        getUser(userId);
        List<ItemRequestDtoLite> liteList = requestRepository.findByRequesterId(userId,
                        Sort.by(Sort.Direction.DESC, "created")).stream().map(ItemRequestMapper::toItemRequestDtoLite)
                .toList();
        return liteList;
    }

    @Override
    public List<ItemRequestDto> findItemRequestOwnerUser(Long userId, int from, int size) {
        getUser(userId);
        List<ItemRequestDto> itemRequests = requestRepository
                .findByRequester_IdNot(userId, PageRequest.of(from, size, Sort.by("created").descending()))
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
        return itemRequests;

    }

    @Override
    public ItemRequestDtoOut getItemRequest(Long userId, Long requestId) {
        getUser(userId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Нет request с заданным id: " + requestId));
        ItemRequestDtoOut itemRequestDtoOut = ItemRequestMapper.toRequestDtoOut(itemRequest);
        return itemRequestDtoOut;

    }


    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));

    }
}
