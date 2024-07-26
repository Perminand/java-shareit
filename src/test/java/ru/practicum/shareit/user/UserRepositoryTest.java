//package ru.practicum.shareit.user;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.TestComponent;
//import ru.practicum.shareit.user.model.User;
//
//@TestComponent
//@RequiredArgsConstructor
//class UserRepositoryTest {
//    private final UserRepository userRepository = new UserRepositoryImpl();
//
//    @Test
//    void create() {
//        int count = userRepository.getAll().size();
//        User user = new User();
//        user.setName("name");
//        user.setEmail("ru1@ru.ru");
//        userRepository.create(user);
//        Assertions.assertEquals(count + 1, userRepository.getAll().size());
//    }
//
//    @Test
//    void getAll() {
//        create();
//    }
//
//
//    @Test
//    void getById() {
//        User user = new User();
//        user.setName("name");
//        user.setEmail("ru1@ru.ru");
//        userRepository.create(user);
//        User user2 = new User();
//        user2.setName("name2");
//        user2.setEmail("ru2@ru.ru");
//        userRepository.create(user2);
//        Assertions.assertEquals(user2.getEmail(), userRepository.getById(2L).get().getEmail());
//    }
//
//    @Test
//    void update() {
//        User user = new User();
//        user.setName("name");
//        user.setEmail("ru1@ru.ru");
//        userRepository.create(user);
//        User newUser = user;
//        newUser.setEmail("ru2@ru.ru");
//        userRepository.update(newUser);
//        Assertions.assertEquals(newUser.getEmail(), userRepository.getById(1L).get().getEmail());
//    }
//
//    @Test
//    void deleteById() {
//        User user = new User();
//        user.setName("name");
//        user.setEmail("ru1@ru.ru");
//        userRepository.create(user);
//        int counts = userRepository.getAll().size();
//        userRepository.deleteById(1L);
//        Assertions.assertEquals(counts - 1, userRepository.getAll().size());
//    }
//}