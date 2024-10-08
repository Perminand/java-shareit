package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.comment.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mappers.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private final UserService userService;

    @Override
    public ItemDto getItemById(long itemId, long userId) {
        getUser(userId);
        List<CommentDto> commentsForItem = commentRepository.findAllByItem_Id(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        List<BookingDtoOut> bookingsForItem = getOwnerBooking(userId)
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
        User userFromDb = getUser(userId);

        List<Item> userItems = itemRepository.findByOwnerId(userFromDb.getId(),
                Sort.by(Sort.Direction.ASC,
                        "id"));
        List<CommentDto> commentsToUserItems = commentRepository.findAllByItemsUserId(userId, Sort.by(Sort.Direction.DESC, "created"))
                .stream().map(CommentMapper::toCommentDto).toList();
        List<BookingDtoOut> bookingsToUserItems = getOwnerBooking(userId);

        Map<Item, List<BookingDtoOut>> itemsWithBookingsMap = new HashMap<>();
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
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = getUser(userId);
        Item item = toItem(itemDto);
        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = requestGet(itemDto.getRequestId());
        }
        item.setOwner(user);
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(request);
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }


    @Override
    @Transactional
    public ItemDtoLite update(Long userId, long itemId, ItemDto itemDto) {
        User user = getUser(userId);
        Item item = getItem(itemId);
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
        return toItemDtoLite(item);
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

    @Transactional
    @Override
    public CommentDtoOut createComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            throw new ValidationException("Comment text cant be empty!");
        }
        User author = getUser(userId);
        List<BookingDtoOut> bookings = bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(userId, itemId, LocalDateTime.now())
                .stream()
                .map(BookingMapper::toBookingDto)
                .toList();
        if (bookings.isEmpty()) {
            throw new ValidationException("This user has no booking");
        }
        Item item = getItem(itemId);
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toCommentDb(commentDto, author, item);
        Comment commentDtoOut = commentRepository.save(comment);
        CommentDtoOut commentDtoOut1 = CommentMapper.toCommentDtoOut(commentDtoOut);
        return commentDtoOut1;
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByOwnerIdAndId(userId, itemId);
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));

    }

    private ItemRequest requestGet(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Нет request с заданным id: " + requestId));

    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Нет item с заданным id: " + itemId));

    }

    private List<BookingDtoOut> getOwnerBooking(Long ownerId) {
        return bookingRepository.findAllByItemOwnerId(ownerId)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
