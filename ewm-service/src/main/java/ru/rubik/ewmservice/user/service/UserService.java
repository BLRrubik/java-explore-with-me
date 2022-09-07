package ru.rubik.ewmservice.user.service;

import ru.rubik.ewmservice.user.dto.UserDto;
import ru.rubik.ewmservice.user.requests.UserCreateRequest;

import java.util.List;

public interface UserService {
    List<UserDto> search(List<Long> ids, Integer from, Integer size);

    UserDto createUser(UserCreateRequest request);

    void deleteUser(Long userId);

    UserDto activateUser(Long userId);
}
