package study.day07.fruit.controller.dto;

import java.time.LocalDate;

public record FruitSaveResponse(String name, LocalDate warehousingDate, Long price) {
}
