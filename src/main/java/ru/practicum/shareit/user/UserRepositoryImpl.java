package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    Long idGenerate = 0L;

    @Override
    public List<User> getAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public User create(User user) {
        user.setId(++idGenerate);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public User update(User user) {
        userMap.replace(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public void deleteById(long userId) {
        userMap.remove(userId);
    }
}
