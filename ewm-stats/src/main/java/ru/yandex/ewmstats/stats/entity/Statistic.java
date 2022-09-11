package ru.yandex.ewmstats.stats.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistic_id")
    private Long id;

    @Column(name = "uri")
    private String uri;

    @Column(name = "app")
    private String app;

    @Column(name = "ip")
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
