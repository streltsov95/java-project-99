package hexlet.code.component;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusResolver {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public TaskStatus fromSlug(String status) {
        return taskStatusRepository.findBySlug(status)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task status with slug " + status + " not found"));
    }
}
