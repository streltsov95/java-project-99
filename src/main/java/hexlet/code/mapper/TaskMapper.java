package hexlet.code.mapper;

import hexlet.code.component.LabelResolver;
import hexlet.code.component.TaskStatusResolver;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
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
import java.util.stream.Collectors;

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
    @Mapping(target = "taskLabelIds", source = "labels")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", source = "taskLabelIds")
    public abstract Task map(TaskDTO dto);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", ignore = true)
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    protected Set<Long> map(Set<Label> labels) {
        return labels == null ? null : labels.stream().map(Label::getId).collect(Collectors.toSet());
    }

    @AfterMapping
    protected void addLabels(TaskCreateDTO dto, @MappingTarget Task task) {
        if (dto.getTaskLabelIds() != null && dto.getTaskLabelIds().isPresent()) {
            dto.getTaskLabelIds().get().forEach(labelId -> {
                Label label = labelRepository.findById(labelId)
                        .orElseThrow(() -> new ResourceNotFoundException("Label with id " + labelId + " not found"));
                task.addLabel(label);
            });
        }
    }

    @AfterMapping
    protected void updateLabels(TaskUpdateDTO dto, @MappingTarget Task task) {
        if (dto.getLabelIds() == null || !dto.getLabelIds().isPresent()) {
            return;
        }
        Set<Long> labelIds = dto.getLabelIds().get();
        task.getLabels().clear();
        if (labelIds != null) {
            var labels = labelRepository.findAllById(labelIds);
            labels.forEach(task::addLabel);
        }
    }
}
