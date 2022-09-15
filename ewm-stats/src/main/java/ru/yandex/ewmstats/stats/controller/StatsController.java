package ru.yandex.ewmstats.stats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmstats.stats.dto.ViewStats;
import ru.yandex.ewmstats.stats.requests.EndpointHit;
import ru.yandex.ewmstats.stats.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class StatsController {
    private final StatisticService statisticService;

    @Autowired
    public StatsController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(@RequestParam("start") Long start,
                                                    @RequestParam("end") Long end,
                                                    @RequestParam("uris") List<String> uris,
                                                    @RequestParam(value = "unique", defaultValue = "false")
                                                        Boolean unique) {
        return ResponseEntity.of(Optional.of(statisticService.getStats(start, end, uris, unique)));
    }

    @PostMapping("/hit")
    public void saveStats(@RequestBody EndpointHit request) {
        statisticService.hit(request);
    }
}
