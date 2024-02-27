package study.day07.fruit.controller.dto;

public record FruitCountResponse(Long count) {
    public static FruitCountResponse of(Long count) {
        return new FruitCountResponse(count);
    }
}
