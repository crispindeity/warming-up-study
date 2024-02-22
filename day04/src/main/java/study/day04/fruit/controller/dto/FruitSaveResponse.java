package study.day04.fruit.controller.dto;

import java.time.LocalDate;

public record FruitSaveResponse(String name, LocalDate warehousingDate, Long price) {
}
