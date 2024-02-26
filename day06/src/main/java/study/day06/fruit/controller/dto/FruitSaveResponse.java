package study.day06.fruit.controller.dto;

import java.time.LocalDate;

public record FruitSaveResponse(String name, LocalDate warehousingDate, Long price) {
}
