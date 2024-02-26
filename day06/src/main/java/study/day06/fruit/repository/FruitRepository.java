package study.day06.fruit.repository;

import study.day06.fruit.domain.Fruit;

import java.util.Optional;

public interface FruitRepository {
    Fruit save(Fruit fruit);
    Long update(Long id);
    Optional<Long> findSoldsAmountByFruitName(String name);
    Optional<Long> findSalesAmountByFruitName(String name);
    Optional<Fruit> findById(Long id);
}
