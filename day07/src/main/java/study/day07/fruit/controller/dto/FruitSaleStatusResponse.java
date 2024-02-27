package study.day07.fruit.controller.dto;

import java.util.List;

public record FruitSaleStatusResponse(List<FruitSaveResponse> result) {
    public static FruitSaleStatusResponse of(List<FruitSaveResponse> result) {
        return new FruitSaleStatusResponse(result);
    }
}
