package ru.yandex.ewmservice.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.user.dto.UserDto;
import ru.yandex.ewmservice.user.requests.UserCreateRequest;
import ru.yandex.ewmservice.user.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam("ids") List<Long> ids,
                                                     @RequestParam("from") Integer from,
                                                     @RequestParam("size") Integer size) {
        return ResponseEntity.of(Optional.of(userService.search(ids, from, size)));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.of(Optional.of(userService.createUser(request)));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.of(Optional.of(userService.activateUser(userId)));
    }
}
