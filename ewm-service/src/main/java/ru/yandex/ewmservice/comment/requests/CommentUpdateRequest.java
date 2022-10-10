package ru.yandex.ewmservice.comment.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {
    @Positive
    @NotNull
    private Long id;
    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String text;
}
