package study.day04.fruit.service;

import org.springframework.stereotype.Service;
import study.day04.fruit.controller.dto.FruitSaveResponse;
import study.day04.fruit.controller.dto.FruitStatResponse;
import study.day04.fruit.controller.dto.FruitUpdateResponse;
import study.day04.fruit.domain.Fruit;
import study.day04.fruit.repository.FruitRepository;
import study.day04.fruit.service.dto.FruitSaveRequest;
import study.day04.fruit.service.dto.FruitUpdateRequest;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    public FruitServiceImpl(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Override
    public FruitSaveResponse save(FruitSaveRequest request) {
        Fruit savedFruit = fruitRepository.save(Fruit.of(request.name(), request.warehousingDate(), request.price()));
        return new FruitSaveResponse(savedFruit.name(), savedFruit.warehousingDate(), savedFruit.price());
    }

    @Override
    public FruitUpdateResponse update(FruitUpdateRequest request) {
        Long updatedId = fruitRepository.update(request.id());
        return FruitUpdateResponse.of(updatedId);
    }

    @Override
    public FruitStatResponse getStat(String name) {
        Long salesAmount = fruitRepository.findSoldsAmountByFruitName(name).orElse(0L);
        Long notSalesAmount = fruitRepository.findSalesAmountByFruitName(name).orElse(0L);
        return FruitStatResponse.of(salesAmount, notSalesAmount);
    }
}
