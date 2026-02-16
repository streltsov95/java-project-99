package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO params) {
        return withAssigneeId(params.getAssigneeId())
                .and(withTitleCont(params.getTitleCont()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return ((root, query, cb) -> assigneeId == null ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId));
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return ((root, query, cb) -> titleCont == null ? cb.conjunction()
                : cb.like(root.get("name"), titleCont));
    }

    private Specification<Task> withStatus(String status) {
        return ((root, query, cb) -> status == null ? cb.conjunction()
                : cb.equal(root.get("taskStatus").get("slug"), status));
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> {
            if (labelId == null) {
                return cb.conjunction();
            } else {
                query.distinct(true);
                Join<Task, Label> labelJoin = root.join("labels");
                return cb.equal(labelJoin.get("id"), labelId);
            }
        };
    }
}
