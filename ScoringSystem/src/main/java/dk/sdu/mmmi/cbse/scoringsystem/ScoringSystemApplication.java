package dk.sdu.mmmi.cbse.scoringsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RestController
public class ScoringSystemApplication {

    private final AtomicLong totalScore = new AtomicLong();

    public static void main(String[] args) {
        SpringApplication.run(ScoringSystemApplication.class, args);
    }

    @GetMapping("/score")
    public long addScore(@RequestParam(value = "point", defaultValue = "0") long point) {
        return totalScore.addAndGet(Math.max(0, point));
    }

    @GetMapping("/score/reset")
    public long resetScore() {
        totalScore.set(0);
        return totalScore.get();
    }
}
