package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        log.info(" ==> GET /items ");
        List<Item> itemList = itemRepository.getItemsByOwner(userId);
        return itemList.stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto getById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Нет item с заданным id"));
        return ItemMapper.toItemDto(item);
    }


    @Override
    @Transactional(readOnly = false)
    public ItemDto create(Long userId, ItemDto item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new ValidationException("name не может быть null");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ValidationException("description не может быть null");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("available не может быть null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));
        Item newItem = ItemMapper.forItem(item);
        newItem.setOwner(user);
        newItem.setAvailable(item.getAvailable());
        Item finishItem = itemRepository.save(newItem);
        return ItemMapper.toItemDto(finishItem);
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, long itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Нет item с заданным id"));
        if (!Objects.equals(item.getOwner().getId(), user.getId())) {
            throw new EntityNotFoundException("Попытка изменения item не владельцем");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item finishItem = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        List<ItemDto> itemDtoList = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto).toList();
        return itemDtoList;

    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByOwnerAndId(userId, itemId);
    }

//    List<ItemInfoDto> getAll() {
//        List<Item> items = itemRepository.findAll();
//        List<Long> itemIds = items.stream().map(Item::getId).toList();
//        List<Booking> bookings = bookingRepository.findAllByIdsIn(itemIds);
//        Map<Long, List<Booking>> ItemIdByBookings;
//
//    }

}
