package ru.yandex.ewmstats.stats.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.ewmstats.stats.dto.ViewStats;
import ru.yandex.ewmstats.stats.entity.Statistic;
import ru.yandex.ewmstats.stats.repository.StatisticRepository;
import ru.yandex.ewmstats.stats.requests.EndpointHit;
import ru.yandex.ewmstats.stats.service.StatisticService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

    @Autowired
    public StatisticServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void hit(EndpointHit request) {
        Statistic statistic = new Statistic();

        statistic.setApp(request.getApp());
        statistic.setIp(request.getIp());
        statistic.setUri(request.getUri());
        statistic.setTimestamp(LocalDateTime.now());

        statisticRepository.save(statistic);
    }

    @Override
    public List<ViewStats> getStats(String start,
                                    String end,
                                    List<String> uris,
                                    Boolean unique) {

        List<ViewStats> hits = new ArrayList<>();

        for (String uri : uris) {
            String app = "ewm-service";
            Integer count = unique ? statisticRepository.getStatisticDistinct(start, end, app, uri) :
                    statisticRepository.getAllStatistic(start, end, app, uri);

            hits.add(new ViewStats(
                    app,
                    uri,
                    count
            ));
        }

        hits.forEach(stat -> System.out.println(stat.getUri() + ", " + stat.getApp() + ", " + stat.getHits()));

        return hits;
    }
}
