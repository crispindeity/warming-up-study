package study.day07.fruit.controller.dto;

import java.time.LocalDate;

public record FruitSaveResponse(String name, LocalDate warehousingDate, Long price) {
    public static FruitSaveResponse of(String name, LocalDate warehousingDate, Long price) {
        return new FruitSaveResponse(name, warehousingDate, price);
    }
}
