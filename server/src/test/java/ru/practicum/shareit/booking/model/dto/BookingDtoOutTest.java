package ru.practicum.shareit.booking.model.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoOutTest {

    private static final String DATE_TIME = "2024-08-20T07:33:00";
    @Autowired
    private JacksonTester<BookingDtoOut> json;
    private BookingDtoOut bookingDtoOut = null;

    @BeforeEach
    public void init() {
        bookingDtoOut = BookingDtoOut.builder()
                .itemId(1L)
                .start(LocalDateTime.parse("2024-08-20T07:33:00"))
                .end(LocalDateTime.parse("2024-08-20T07:33:00"))
                .build();
    }

    @Test
    @SneakyThrows
    public void startSerializes() {
        assertThat(json.write(bookingDtoOut)).extractingJsonPathStringValue("$.start")
                .isEqualTo(DATE_TIME);
    }

    @Test
    @SneakyThrows
    public void endSerializes() {
        assertThat(json.write(bookingDtoOut)).extractingJsonPathStringValue("$.end")
                .isEqualTo(DATE_TIME);
    }
}