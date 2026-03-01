package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {

    private JsonNullable<Integer> index;

    private JsonNullable<Long> assignee_id;

    @NotBlank
    private JsonNullable<String> title;

    private JsonNullable<String> content;

    private JsonNullable<Set<Long>> taskLabelIds;

    @NotBlank
    private JsonNullable<String> status;
}
