package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors());
    }

    // ==== Индивидуальное задание, вариант 6 ====
    @GetMapping("/stats")
    public Map<String, Object> stats(@RequestParam(defaultValue = "0") String numbers) {
        String[] parts = numbers.split(",");
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        double sum = 0;
        int count = 0;

        for (String part : parts) {
            double value = Double.parseDouble(part.trim());
            if (value < min) min = value;
            if (value > max) max = value;
            sum += value;
            count++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("min", min);
        result.put("max", max);
        result.put("average", sum / count);
        return result;
    }

    public record Greeting(String message, String owner, LocalDateTime timestamp) { }

    public record Info(String owner, String javaVersion, int cpuCores) { }
}