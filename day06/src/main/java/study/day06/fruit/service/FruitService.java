package study.day06.fruit.service;

import study.day06.fruit.controller.dto.FruitSaveResponse;
import study.day06.fruit.controller.dto.FruitStatResponse;
import study.day06.fruit.controller.dto.FruitUpdateResponse;
import study.day06.fruit.service.dto.FruitSaveRequest;
import study.day06.fruit.service.dto.FruitUpdateRequest;

public interface FruitService {
    FruitSaveResponse save(FruitSaveRequest request);
    FruitUpdateResponse update(FruitUpdateRequest request);
    FruitStatResponse getStat(String name);
}
