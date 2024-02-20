package study.controller;

import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class AssignmentController {

    @GetMapping("/calc")
    public ResponseCalcResult calc(@ModelAttribute Numbers numbers) {
        int added = numbers.num1() + numbers.num2();
        int minused = numbers.num1() - numbers.num2();
        int multiplied = numbers.num1() * numbers.num2();
        return new ResponseCalcResult(added, minused, multiplied);
    }

    @GetMapping("/day-of-week")
    public ResponseDateResult date(@RequestParam(name = "date") String date) {
        DayOfWeek dayOfWeek = LocalDate.parse(date).getDayOfWeek();
        return new ResponseDateResult(dayOfWeek);
    }

    @PostMapping("/calc")
    public Integer sum(@RequestBody NumberBundle numberBundle) {
        return numberBundle.numbers().stream().reduce(0, Integer::sum);
    }
}
