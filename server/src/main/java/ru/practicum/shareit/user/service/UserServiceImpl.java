package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mappers.UserMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("Email is empty");
        }
        validate(userDto);
        log.debug("Creating user : {}", userDto);
        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public UserDto getById(long id) {
        log.debug("Getting user by Id: {}", id);
        User userFromRep = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no User with id: " + id));
        return toUserDto(userFromRep);
    }

    public List<UserDto> getAllUsers() {
        log.debug("Getting all users");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto) {
        log.debug("Updating user: {}", userDto);
        User userToUpdate = toUserUpdate(userDto, userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("There is no User with id: " + userDto.getId())));
        userRepository.save(userToUpdate);
        return toUserDto(userToUpdate);
    }

    @Override
    public void deleteById(long id) {
        User userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no User with id: " + id));
        log.debug("Deleting user by id: {}", id);
        userRepository.deleteById(userFromDb.getId());
    }

    private void validate(UserDto user) {
        if (!Pattern.matches("^(.+)@(\\S+)$", user.getEmail())) {
            throw new ValidationException("Емайл не корректен");
        }
    }

}
