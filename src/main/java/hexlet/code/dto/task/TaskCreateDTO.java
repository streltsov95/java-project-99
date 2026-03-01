package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {

    private JsonNullable<Integer> index;

    private JsonNullable<Long> assignee_id;

    @NotBlank
    private String title;

    private JsonNullable<String> content;

    private JsonNullable<Set<Long>> taskLabelIds;

    @NotBlank
    private String status;
}
