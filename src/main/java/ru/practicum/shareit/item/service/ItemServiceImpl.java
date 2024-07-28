package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mappers.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mappers.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto getItemById(long itemId, long userId) {
        userCheck(userId);
        List<CommentDto> commentsForItem = commentRepository.findAllByItem_Id(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        List<BookingDto> bookingsForItem = getOwnerBooking(userId)
                .stream()
                .filter(x -> x.getItem().getId().equals(itemId))
                .collect(Collectors.toList());

        if (!bookingsForItem.isEmpty() && !commentsForItem.isEmpty()) {
            return toItemDtoWithBookingsAndComments(itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no Item with Id: " + itemId)), bookingsForItem, commentsForItem);
        } else if (!bookingsForItem.isEmpty()) {
            return toItemDtoWithBookings(itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no Item with Id: " + itemId)), bookingsForItem);
        } else if (!commentsForItem.isEmpty()) {
            return toItemDtoWithComments(itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no Item with Id: " + itemId)), commentsForItem);
        } else {
            return toItemDto(itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no Item with Id: " + itemId)));
        }
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        User userFromDb = userCheck(userId);

        List<Item> userItems = itemRepository.findByOwner_Id(userFromDb.getId(), Sort.by(Sort.Direction.ASC, "id"));
        List<CommentDto> commentsToUserItems = commentRepository.findAllByItemsUserId(userId, Sort.by(Sort.Direction.DESC, "created"))
                .stream().map(CommentMapper::toCommentDto).toList();
        List<BookingDto> bookingsToUserItems = getOwnerBooking(userId);

        Map<Item, List<BookingDto>> itemsWithBookingsMap = new HashMap<>();
        Map<Item, List<CommentDto>> itemsWithCommentsMap = new HashMap<>();

        for (Item i : userItems) {
            itemsWithCommentsMap.put(i, commentsToUserItems.stream()
                    .filter(c -> c.getItem().getId().equals(i.getId()))
                    .collect(Collectors.toList()));
            itemsWithBookingsMap.put(i, bookingsToUserItems.stream()
                    .filter(b -> b.getItem().getId().equals(i.getId()))
                    .collect(Collectors.toList()));
        }

        List<ItemDto> results = new ArrayList<>();
        for (Item i : userItems) {
            results.add(toItemDtoWithBookingsAndComments(i, itemsWithBookingsMap.get(i), itemsWithCommentsMap.get(i)));
        }

        return results;
    }

    @Override
    @Transactional
    public ItemDtoLite create(Long userId, ItemDto item) {
        validate(item);
        User user = userCheck(userId);
        Item newItem = ItemMapper.toItem(item);
        newItem.setOwner(user);
        newItem.setAvailable(item.getAvailable());
        itemRepository.save(newItem);
        return ItemMapper.toItemDtoLite(newItem);
    }

    @Override
    @Transactional
    public ItemDtoLite update(Long userId, long itemId, ItemDto itemDto) {
        User user = userCheck(userId);
        Item item = itemCheck(itemId);
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
        itemRepository.save(item);
        return ItemMapper.toItemDtoLite(item);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto).toList();

    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            throw new ValidationException("Comment text cant be empty!");
        }
        User author = userCheck(userId);
        List<BookingDto> bookings = bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(userId, itemId, LocalDateTime.now())
                .stream()
                .map(BookingMapper::toBookingDto)
                .toList();
        if (bookings.isEmpty()) {
            throw new ValidationException("This user has no booking");
        }
        Item item = itemCheck(itemId);
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toCommentDb(commentDto, author, item);
        Comment commentDtoOut = commentRepository.save(comment);
        commentDto = toCommentDto(commentDtoOut);
        return commentDto;
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByOwnerAndId(userId, itemId);
    }

    private User userCheck(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));

    }

    private Item itemCheck(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Нет item с заданным id: " + itemId));

    }

    private void validate(ItemDto item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new ValidationException("name не может быть null");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ValidationException("description не может быть null");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("available не может быть null");
        }
    }

    private List<BookingDto> getOwnerBooking(Long ownerId) {
        return bookingRepository.findAllByItem_Owner_Id(ownerId)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
