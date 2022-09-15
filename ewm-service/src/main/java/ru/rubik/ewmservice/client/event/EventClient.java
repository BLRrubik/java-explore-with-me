package ru.rubik.ewmservice.client.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.rubik.ewmservice.client.BaseClient;
import ru.rubik.ewmservice.client.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventClient extends BaseClient {

    private static final String API_PREFIX = "/";

    @Autowired
    public EventClient(@Value("${ewm-stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public List<StatsDto> getStats(List<String> uris) {
        String start = LocalDateTime.now().minusDays(10L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String path = "stats?start=" + start + "&end=" + end + "&uris=" + String.join("&uris=", uris);

        var response = get(path);

        ObjectMapper mapper = new ObjectMapper();

        return Arrays.stream(response.getBody())
                .map(object -> mapper.convertValue(object, StatsDto.class))
                .collect(Collectors.toList());
    }

    public void hit(HttpServletRequest httpRequest) {
        String app = "ewm-service";
        String uri = httpRequest.getRequestURI();
        String ip = httpRequest.getRemoteAddr();

        Map<String, String> body = new HashMap<>();

        body.put("app", app);
        body.put("uri", uri);
        body.put("ip", ip);

        post("hit", body);
    }
}
