package study.day04.fruit.service;

import study.day04.fruit.controller.dto.FruitSaveResponse;
import study.day04.fruit.controller.dto.FruitStatResponse;
import study.day04.fruit.controller.dto.FruitUpdateResponse;
import study.day04.fruit.service.dto.FruitSaveRequest;
import study.day04.fruit.service.dto.FruitUpdateRequest;

public interface FruitService {
    FruitSaveResponse save(FruitSaveRequest request);
    FruitUpdateResponse update(FruitUpdateRequest request);
    FruitStatResponse getStat(String name);
}
