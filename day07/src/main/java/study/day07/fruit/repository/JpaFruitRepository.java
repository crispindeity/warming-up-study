package study.day07.fruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.day07.fruit.domain.Status;
import study.day07.fruit.repository.entity.FruitEntity;

import java.util.List;

public interface JpaFruitRepository extends JpaRepository<FruitEntity, Long> {

    Long countByName(String name);
    List<FruitEntity> findAllByStatusAndPriceGreaterThanEqual(Status status, Long price);
    List<FruitEntity> findAllByStatusAndPriceLessThanEqual(Status status, Long price);
    List<FruitEntity> findAllByStatusAndName(Status status, String name);
}
