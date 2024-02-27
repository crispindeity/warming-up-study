package study.day07.fruit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.day07.fruit.controller.dto.*;
import study.day07.fruit.domain.Fruit;
import study.day07.fruit.repository.FruitRepository;
import study.day07.fruit.service.dto.FruitSaleStatusRequest;
import study.day07.fruit.service.dto.FruitSaveRequest;
import study.day07.fruit.service.dto.FruitUpdateRequest;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FruitServiceImpl implements FruitService {

    private static final String GTE = "GTE";
    private final FruitRepository fruitRepository;

    public FruitServiceImpl(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Override
    @Transactional
    public FruitSaveResponse save(FruitSaveRequest request) {
        Fruit savedFruit = fruitRepository.save(Fruit.of(request.name(), request.warehousingDate(), request.price()));
        return FruitSaveResponse.of(savedFruit.name(), savedFruit.warehousingDate(), savedFruit.price());
    }

    @Override
    @Transactional
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

    @Override
    public FruitCountResponse getCount(String name) {
        return FruitCountResponse.of(fruitRepository.count(name));
    }

    @Override
    public FruitSaleStatusResponse getSaleStatus(FruitSaleStatusRequest request) {
        if (request.option().equals(GTE)) {
            return convertToSaleStatusResponse(fruitRepository.findAllByStatusAndPriceGreaterThanEqual(request.price()));
        }
        return convertToSaleStatusResponse(fruitRepository.findAllByStatusAndPriceLessThanEqual(request.price()));
    }

    private FruitSaleStatusResponse convertToSaleStatusResponse(List<Fruit> fruits) {
        return FruitSaleStatusResponse.of(
                fruits.stream()
                        .map(fruit -> FruitSaveResponse.of(fruit.name(), fruit.warehousingDate(), fruit.price()))
                        .toList()
        );
    }
}
