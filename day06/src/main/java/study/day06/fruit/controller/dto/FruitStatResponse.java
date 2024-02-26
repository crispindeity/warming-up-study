package study.day06.fruit.controller.dto;

public record FruitStatResponse(Long salesAmount, Long notSalesAmount) {
    public static FruitStatResponse of(Long salesAmount, Long notSalesAmount) {
        return new FruitStatResponse(salesAmount, notSalesAmount);
    }
}
