package hexlet.code.service.userservice;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskRepository taskRepository;

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
    }

    @Override
    public UserDTO findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        return userMapper.map(user);
    }
    @Override
    public UserDTO create(UserCreateDTO userData) {
        var user = userMapper.map(userData);
        userRepository.save(user);

        return userMapper.map(user);
    }

    @Override
    public UserDTO update(UserUpdateDTO userData, Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        userMapper.update(userData, user);
        userRepository.save(user);

        return userMapper.map(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
