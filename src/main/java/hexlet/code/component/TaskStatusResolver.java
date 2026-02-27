package hexlet.code.component;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskStatusResolver {

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus fromSlug(String status) {
        return taskStatusRepository.findBySlug(status)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task status with slug " + status + " not found"));
    }
}
