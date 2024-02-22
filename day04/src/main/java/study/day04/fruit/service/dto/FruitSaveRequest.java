package study.day04.fruit.service.dto;

import java.time.LocalDate;

public record FruitSaveRequest(String name, LocalDate warehousingDate, Long price) {
}
