package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("==> /users ");
        List<UserDto> finalUser = userRepository
                .findAll()
                .stream()
                .map(user -> UserMapper.toUserDto((User) user))
                .toList();
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
        User finalUser = (User) userRepository.save(user);
        log.info(" <== {}", finalUser);
        return finalUser;
    }

    @Override
    public UserDto getById(long userId) {
        log.info(" ==> /users/{}", userId);
        User u = null;
        try {
            u = (User) userRepository.findById(userId).get();
        } catch (Throwable e) {
            throw new EntityNotFoundException("user not found id :" + userId);
        }
        UserDto finalUser = UserMapper.toUserDto(u);
        log.info(" <== {}", finalUser);
        return finalUser;
    }

    @Override
    public User update(long userId, User user) {
        log.info(" ==> /users/{} {}", userId, user);
        User oldUser = (User) userRepository.getById(userId);
        if (user.getEmail() != null) {
            if (!oldUser.getEmail().equals(user.getEmail())) {
                validate(user);
                oldUser.setEmail(user.getEmail());
            }
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        User finalUser = (User) userRepository.save(oldUser);
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
        if (!Pattern.matches("^(.+)@(\\S+)$", user.getEmail())) {
            throw new ValidationException("Емайл не коррктен");
        }
    }
}
