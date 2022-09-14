package ru.yandex.ewmstats.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.ewmstats.stats.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query(nativeQuery = true,
    value = "select count(distinct (s.ip)) from stats as s " +
            "where date(s.timestamp) between date(?) and date(?) " +
            "and app = ? and uri = ?")
    Integer getStatisticDistinct(String start, String end, String app, String uri);

    @Query(nativeQuery = true,
            value = "select count(s.ip) from stats as s " +
                    "where date(s.timestamp) between date(?) and date(?) " +
                    "and s.app = ? and s.uri = ?")
    Integer getAllStatistic(String start, String end, String app, String uri);

    @Query(nativeQuery = true,
    value = "select s.app from stats as s " +
            "where s.uri ilike ? " +
            "limit 1")
    String getAppNameByUri(String uri);
}
