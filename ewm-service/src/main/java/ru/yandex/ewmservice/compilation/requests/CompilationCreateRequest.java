package ru.yandex.ewmservice.compilation.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreateRequest {
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
