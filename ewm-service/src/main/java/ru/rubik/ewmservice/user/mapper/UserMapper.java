package ru.rubik.ewmservice.user.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.rubik.ewmservice.user.dto.UserDto;
import ru.rubik.ewmservice.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static List<UserDto> toDtos(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<UserDto> convertPageToDto(Page<User> page) {

        if (page.isEmpty())
        {
            return Page.empty();
        }

        return new PageImpl<>(toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
