package hexlet.code.service.taskstatusservice;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    @Override
    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .toList();
    }
    @Override
    public TaskStatusDTO findById(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));

        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusData) {
        var taskStatus = taskStatusMapper.map(taskStatusData);

        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO taskStatusData, Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + " not found"));

        taskStatusMapper.update(taskStatusData, taskStatus);
        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.map(taskStatus);
    }

    public void delete(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
