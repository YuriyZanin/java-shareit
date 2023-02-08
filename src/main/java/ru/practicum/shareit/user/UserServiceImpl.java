package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User get(Long id) {
        return userRepository.get(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(Long userId, UserDto userDetails) {
        return userRepository.update(userId, userDetails);
    }

    @Override
    public boolean delete(Long id) {
        return userRepository.delete(id);
    }
}
