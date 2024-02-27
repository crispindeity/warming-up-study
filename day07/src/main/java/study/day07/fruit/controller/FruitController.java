package study.day07.fruit.controller;

import org.springframework.web.bind.annotation.*;
import study.day07.fruit.controller.dto.*;
import study.day07.fruit.service.FruitService;
import study.day07.fruit.service.dto.FruitSaleStatusRequest;
import study.day07.fruit.service.dto.FruitSaveRequest;
import study.day07.fruit.service.dto.FruitUpdateRequest;

@RestController
@RequestMapping("/api/v1")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping("/fruit")
    public FruitSaveResponse save(@RequestBody FruitSaveRequest request) {
        return fruitService.save(request);
    }

    @PutMapping("/fruit")
    public FruitUpdateResponse update(@RequestBody FruitUpdateRequest request) {
        return fruitService.update(request);
    }

    @GetMapping("/fruit/stat")
    public FruitStatResponse getStat(@RequestParam String name) {
        return fruitService.getStat(name);
    }

    @GetMapping("/fruit/count")
    public FruitCountResponse getCount(@RequestParam String name) {
        return fruitService.getCount(name);
    }

    @GetMapping("/fruit/list")
    public FruitSaleStatusResponse getSaleStatusFruits(FruitSaleStatusRequest request) {
        return fruitService.getSaleStatus(request);
    }
}
