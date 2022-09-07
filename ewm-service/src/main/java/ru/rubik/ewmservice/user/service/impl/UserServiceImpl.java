package ru.rubik.ewmservice.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rubik.ewmservice.user.dto.UserDto;
import ru.rubik.ewmservice.user.entity.User;
import ru.rubik.ewmservice.user.exception.UserEmailUniqException;
import ru.rubik.ewmservice.user.exception.UserNotFoundException;
import ru.rubik.ewmservice.user.mapper.UserMapper;
import ru.rubik.ewmservice.user.repository.UserRepository;
import ru.rubik.ewmservice.user.requests.UserCreateRequest;
import ru.rubik.ewmservice.user.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> search(List<Long> ids, Integer from, Integer size) {
        List<User> users = ids.stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<User> page = users.stream()
                .sorted(Comparator.comparing(User::getId))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());

        return UserMapper.toDtos(page);
    }

    @Override
    public UserDto createUser(UserCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserEmailUniqException("Email " + request.getEmail() + "is already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setActive(false);

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        userRepository.deleteById(userId);
    }

    @Override
    public UserDto activateUser(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        User user = userRepository.findById(userId).get();

        user.setActive(true);

        return UserMapper.toDto(userRepository.save(user));
    }
}
