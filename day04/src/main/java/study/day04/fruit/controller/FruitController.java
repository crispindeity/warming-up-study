package study.day04.fruit.controller;

import org.springframework.web.bind.annotation.*;
import study.day04.fruit.controller.dto.FruitSaveResponse;
import study.day04.fruit.controller.dto.FruitStatResponse;
import study.day04.fruit.controller.dto.FruitUpdateResponse;
import study.day04.fruit.service.FruitService;
import study.day04.fruit.service.dto.FruitSaveRequest;
import study.day04.fruit.service.dto.FruitUpdateRequest;

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
}
