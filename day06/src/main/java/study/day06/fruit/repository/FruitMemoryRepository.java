package study.day06.fruit.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import study.day06.fruit.domain.Fruit;
import study.day06.fruit.domain.Status;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Primary
@Repository
public class FruitMemoryRepository implements FruitRepository {

    private final Map<Long, Fruit> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Fruit save(Fruit fruit) {
        if (fruit.id() == null || storage.get(fruit.id()) == null) {
            long currentSequence = sequence.incrementAndGet();
            Fruit newFruit = Fruit.of(
                    currentSequence,
                    fruit.name(),
                    fruit.warehousingDate(),
                    fruit.price()
            );
            storage.put(currentSequence, newFruit);
            return storage.get(currentSequence);
        } else {
            storage.put(fruit.id(), fruit);
            return storage.get(fruit.id());
        }
    }

    @Override
    public Long update(Long id) {
        Fruit fruit = storage.get(id);

        if (fruit == null) {
            throw new IllegalArgumentException("해당하는 과일이 없습니다.");
        }

        save(fruit.updateStat());
        return id;
    }

    @Override
    public Optional<Long> findSoldsAmountByFruitName(String name) {
        return storage.values()
                .stream()
                .filter(fruit -> fruit.name().equals(name) && fruit.status().equals(Status.SOLD))
                .map(Fruit::price)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Long> findSalesAmountByFruitName(String name) {
        return storage.values()
                .stream()
                .filter(fruit -> fruit.name().equals(name) && fruit.status().equals(Status.SALE))
                .map(Fruit::price)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Fruit> findById(Long id) {
        return Optional.of(storage.get(id));
    }
}
