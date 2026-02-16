package hexlet.code.controller.api;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSpecification specBuilder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<TaskDTO>> index(TaskParamsDTO params, @RequestParam(defaultValue = "1") int page) {
        var spec = specBuilder.build(params);
        var tasks = taskRepository.findAll(spec, PageRequest.of(page - 1, 10));
        var result = tasks.map(taskMapper::map);

        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.getTotalElements())).body(result);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        return taskMapper.map(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);

        taskRepository.save(task);

        return taskMapper.map(task);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO taskData, @PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        taskMapper.update(taskData, task);
        taskRepository.save(task);

        return taskMapper.map(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Task with id " + id + " not found");
        }
    }
}
