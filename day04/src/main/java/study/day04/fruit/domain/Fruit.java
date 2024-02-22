package study.day04.fruit.domain;

import java.time.LocalDate;
import java.util.Map;

public record Fruit(Long id, String name, LocalDate warehousingDate, Long price, Status status) {
    public static Fruit of(String name, LocalDate warehousingDate, Long price) {
        return new Fruit(null, name, warehousingDate, price, Status.SALE);
    }

    public static Fruit of(Long id, String name, LocalDate warehousingDate, Long price) {
        return new Fruit(id, name, warehousingDate, price, Status.SALE);
    }

    public static Fruit updateStat(Fruit fruit) {
        return new Fruit(fruit.id(), fruit.name(), fruit.warehousingDate(), fruit.price(), Status.SOLD);
    }
}
