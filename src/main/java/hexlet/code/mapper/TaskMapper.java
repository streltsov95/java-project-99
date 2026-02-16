package hexlet.code.mapper;

import hexlet.code.component.LabelResolver;
import hexlet.code.component.TaskStatusResolver;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class, TaskStatusResolver.class, LabelResolver.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelResolver labelResolver;

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", ignore = true)
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "labels", source = "labels")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", source = "labels")
    public abstract Task map(TaskDTO dto);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", ignore = true)
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    protected String map(Label label) {
        return label == null ? null : label.getName();
    }
    @AfterMapping
    protected void addLabels(TaskCreateDTO dto, @MappingTarget Task task) {
        if (dto.getLabelNames() != null && dto.getLabelNames().isPresent()) {
            dto.getLabelNames().get().forEach(labelName -> {
                Label label = labelResolver.fromName(labelName);
                task.addLabel(label);
            });
        }
    }

    @AfterMapping
    protected void updateLabels(TaskUpdateDTO dto, @MappingTarget Task task) {
        if (dto.getLabelNames() == null || !dto.getLabelNames().isPresent()) {
            return;
        }
        Set<String> labelNames = dto.getLabelNames().get();
        if (labelNames == null) {
            labelNames.clear();
        }
        task.getLabels().clear();
        labelNames.forEach(labelName -> {
            Label label = labelResolver.fromName(labelName);
            task.addLabel(label);
        });
    }
}
