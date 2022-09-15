package ru.yandex.ewmservice.user.service;

import ru.yandex.ewmservice.user.dto.UserDto;
import ru.yandex.ewmservice.user.requests.UserCreateRequest;

import java.util.List;

public interface UserService {
    List<UserDto> search(List<Long> ids, Integer from, Integer size);

    UserDto createUser(UserCreateRequest request);

    void deleteUser(Long userId);

    UserDto activateUser(Long userId);
}
