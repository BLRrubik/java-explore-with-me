package ru.yandex.ewmstats.stats.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.ewmstats.stats.dto.ViewStats;
import ru.yandex.ewmstats.stats.entity.Statistic;
import ru.yandex.ewmstats.stats.repository.StatisticRepository;
import ru.yandex.ewmstats.stats.requests.EndpointHit;
import ru.yandex.ewmstats.stats.service.StatisticService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    public List<ViewStats> getStats(Long startSeconds, Long endSeconds, List<String> uris, Boolean unique) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String start = LocalDateTime.ofEpochSecond(startSeconds, 0, ZoneOffset.UTC)
                .toString().split("T")[0] + " " +
                LocalDateTime.ofEpochSecond(startSeconds, 0, ZoneOffset.UTC)
                        .toString().split("T")[1];

        String end = LocalDateTime.ofEpochSecond(endSeconds, 0, ZoneOffset.UTC)
                .toString().split("T")[0] + " " +
                LocalDateTime.ofEpochSecond(endSeconds, 0, ZoneOffset.UTC)
                        .toString().split("T")[1];

        System.out.println(start + " --- " + end);

        List<ViewStats> hits = new ArrayList<>();

        for (String uri: uris) {
            System.out.println("uri -- " +uri);
            String app = statisticRepository.getAppNameByUri(uri);
            System.out.println("app - " + app);
            Integer count = unique ? statisticRepository.getStatisticDistinct(start, end, app, uri) :
                    statisticRepository.getAllStatistic(start, end, app, uri);

            hits.add(new ViewStats(
                    app,
                    uri,
                    count
            ));
        }

        return hits;
    }
}
