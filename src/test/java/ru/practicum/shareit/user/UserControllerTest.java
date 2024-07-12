//package ru.practicum.shareit.user;
//
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import lombok.RequiredArgsConstructor;
//import ru.practicum.shareit.user.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.TestComponent;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestComponent
//@RequiredArgsConstructor
//class UserControllerTest {
//    UserController userController;
//    private Validator validator;
//
//    @BeforeEach
//    public void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//    @Test
//    void getAllUsers() {
//    }
//
//    @Test
//    void getById() {
//    }
//
//    @Test
//    void create() {
//        User user = new User();
//        user.setName("name");
//        user.setEmail("ru1@ru.ru");
//        assertTrue(validator.validate(user).isEmpty());
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void deleteById() {
//    }
//}