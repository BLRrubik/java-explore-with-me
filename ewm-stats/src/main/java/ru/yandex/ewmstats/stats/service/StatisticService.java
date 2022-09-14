package ru.yandex.ewmstats.stats.service;

import ru.yandex.ewmstats.stats.dto.ViewStats;
import ru.yandex.ewmstats.stats.requests.EndpointHit;

import java.util.List;

public interface StatisticService {
    void hit(EndpointHit request);

    List<ViewStats> getStats(Long start, Long end, List<String> uris, Boolean unique);
}
