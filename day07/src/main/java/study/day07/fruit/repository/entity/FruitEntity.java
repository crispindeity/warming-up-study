package study.day07.fruit.repository.entity;

import jakarta.persistence.*;
import study.day07.fruit.domain.Fruit;
import study.day07.fruit.domain.Status;

import java.time.LocalDate;

@Entity
@Table(name = "Fruits")
public class FruitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate warehousingDate;
    private Long price;
    @Enumerated(EnumType.STRING)
    private Status status;

    protected FruitEntity() {
    }

    public FruitEntity(Long id, String name, LocalDate warehousingDate, Long price, Status status) {
        this.id = id;
        this.name = name;
        this.warehousingDate = warehousingDate;
        this.price = price;
        this.status = status;
    }

    public static FruitEntity fromModel(Fruit fruit) {
        return new FruitEntity(
                fruit.id(),
                fruit.name(),
                fruit.warehousingDate(),
                fruit.price(),
                fruit.status()
        );
    }

    public Fruit toModel() {
        return Fruit.of(
                this.id,
                this.name,
                this.warehousingDate,
                this.price
        );
    }

    public Long getPrice() {
        return price;
    }
}
