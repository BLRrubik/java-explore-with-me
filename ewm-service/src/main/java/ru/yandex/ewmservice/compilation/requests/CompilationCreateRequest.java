package ru.yandex.ewmservice.compilation.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreateRequest {
    @NotEmpty
    @NotNull
    private String title;
    @NotNull
    private Boolean pinned;
    @NotNull
    private List<Long> events;
}
