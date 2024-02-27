package study.day07.fruit.controller.dto;

public record FruitUpdateResponse(Long id) {
    public static FruitUpdateResponse of(Long updatedId) {
        return new FruitUpdateResponse(updatedId);
    }
}
