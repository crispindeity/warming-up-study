package study.day07.fruit.service;

import study.day07.fruit.controller.dto.*;
import study.day07.fruit.service.dto.FruitSaleStatusRequest;
import study.day07.fruit.service.dto.FruitSaveRequest;
import study.day07.fruit.service.dto.FruitUpdateRequest;

public interface FruitService {
    FruitSaveResponse save(FruitSaveRequest request);
    FruitUpdateResponse update(FruitUpdateRequest request);
    FruitStatResponse getStat(String name);
    FruitCountResponse getCount(String name);
    FruitSaleStatusResponse getSaleStatus(FruitSaleStatusRequest request);
}
