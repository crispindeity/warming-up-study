package study.day07.fruit.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import study.day07.fruit.domain.Fruit;
import study.day07.fruit.domain.Status;
import study.day07.fruit.repository.entity.FruitEntity;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class FruitJpaRepositoryImpl implements FruitRepository {

    private final JpaFruitRepository jpaFruitRepository;

    public FruitJpaRepositoryImpl(JpaFruitRepository jpaFruitRepository) {
        this.jpaFruitRepository = jpaFruitRepository;
    }

    @Override
    public Fruit save(Fruit fruit) {
        FruitEntity fruitEntity = FruitEntity.fromModel(fruit);
        return jpaFruitRepository.save(fruitEntity).toModel();
    }

    @Override
    public Long update(Long id) {
        Fruit findedFruit = jpaFruitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 과일이 없습니다."))
                .toModel();
        Fruit updatedFruit = findedFruit.updateStat();
        jpaFruitRepository.save(FruitEntity.fromModel(updatedFruit));
        return id;
    }

    @Override
    public Optional<Long> findSoldsAmountByFruitName(String name) {
        return jpaFruitRepository.findAllByStatusAndName(Status.SOLD, name)
                .stream()
                .map(FruitEntity::getPrice)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Long> findSalesAmountByFruitName(String name) {
        return jpaFruitRepository.findAllByStatusAndName(Status.SALE, name)
                .stream()
                .map(FruitEntity::getPrice)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Fruit> findById(Long id) {
        return jpaFruitRepository.findById(id)
                .map(FruitEntity::toModel);
    }

    @Override
    public Long count(String name) {
        return jpaFruitRepository.countByName(name);
    }

    @Override
    public List<Fruit> findAllByStatusAndPriceGreaterThanEqual(Long price) {
        return jpaFruitRepository.findAllByStatusAndPriceGreaterThanEqual(Status.SALE, price)
                .stream()
                .map(FruitEntity::toModel)
                .toList();
    }

    @Override
    public List<Fruit> findAllByStatusAndPriceLessThanEqual(Long price) {
        return jpaFruitRepository.findAllByStatusAndPriceLessThanEqual(Status.SALE, price)
                .stream()
                .map(FruitEntity::toModel)
                .toList();
    }
}
