package study.day06.fruit.domain;

import java.time.LocalDate;

public record Fruit(Long id, String name, LocalDate warehousingDate, Long price, Status status) {
    public static Fruit of(String name, LocalDate warehousingDate, Long price) {
        return new Fruit(null, name, warehousingDate, price, Status.SALE);
    }

    public static Fruit of(Long id, String name, LocalDate warehousingDate, Long price) {
        return new Fruit(id, name, warehousingDate, price, Status.SALE);
    }

    public Fruit updateStat() {
        return new Fruit(this.id, this.name, this.warehousingDate, this.price, Status.SOLD);
    }
}
