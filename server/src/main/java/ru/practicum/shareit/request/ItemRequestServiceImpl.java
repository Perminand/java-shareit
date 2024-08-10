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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        User user = userGet(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(user);
        requestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDtoLite> findByUserId(Long userId) {
        userGet(userId);
        List<ItemRequestDtoLite> liteList = requestRepository.findByRequesterId(userId,
                Sort.by(Sort.Direction.DESC, "created")).stream().map(ItemRequestMapper::toItemRequestDtoLite)
                .toList();
        return liteList;
    }

    @Override
    public List<ItemRequestDto> findItemRequestOwnerUser(Long userId, int from, int size) {
        userGet(userId);
        List<ItemRequestDto> itemRequests = requestRepository.findAllNotInUser(userId, PageRequest.of(from, size),
                Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
        return itemRequests;

    }

    @Override
    public ItemRequestDtoLite getItemRequest(Long userId, Long requestId) {
        userGet(userId);
        ItemRequest itemRequest = itemRequestGet(requestId);
        return ItemRequestMapper.toItemRequestDtoLite(itemRequest);

    }

    private User userGet(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));

    }
    private ItemRequest itemRequestGet(long itemRequest) {
        return requestRepository.findById(itemRequest)
                .orElseThrow(() -> new EntityNotFoundException("Нет request с заданным id: " + itemRequest));

    }

}
