package ru.rubik.ewmservice.category.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class CategoryBaseRequest {
    private Long id;
    private String name;
}
