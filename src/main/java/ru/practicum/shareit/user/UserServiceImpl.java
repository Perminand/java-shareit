package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.error.DuplicatedDataException;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("==> /users ");
        List<UserDto> finalUser = userRepository.getAll().stream().map(UserMapper::toUserDto).toList();
        log.info(" <== {}", finalUser);
        return finalUser;
    }

    @Override
    public User create(User user) {
        log.info(" ==> /users/ {}", user);
        if (user.getEmail() == null) {
            throw new ValidationException("Должен быть емайл");
        }
        validate(user);
        User finalUser = userRepository.create(user);
        log.info(" <== {}", finalUser);
        return finalUser;
    }

    @Override
    public UserDto getById(long userId) {
        log.info(" ==> /users/{}", userId);
        UserDto finalUser = UserMapper.toUserDto(userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found id :" + userId)));
        log.info(" <== {}", finalUser);
        return finalUser;
    }

    @Override
    public User update(long userId, User user) {
        log.info(" ==> /users/{} {}", userId, user);
        User oldUser = userRepository.getById(userId).get();
        if (user.getEmail() != null) {
            if (!oldUser.getEmail().equals(user.getEmail())) {
                validate(user);
                oldUser.setEmail(user.getEmail());
            }
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        User finalUser = userRepository.update(oldUser);
        log.info(" <== {}", finalUser);
        return finalUser;
    }

    @Override
    public void deleteById(long userId) {
        log.info(" ==> /users/{}", userId);
        userRepository.deleteById(userId);
        log.info(" <== OK");
    }

    private void validate(User user) {
        if (Pattern.matches("^(.+)@(\\S+)$", user.getEmail())) {
            Optional<User> newUser = userRepository.getAll()
                    .stream()
                    .filter(u -> Objects.equals(u.getEmail(), user.getEmail()))
                    .findFirst();
            if (newUser.isPresent()) {
                throw new DuplicatedDataException("Email " + user.getEmail() + " уже существует");
            }
        } else {
            throw new ValidationException("Емайл не коррктен");
        }
    }
}
