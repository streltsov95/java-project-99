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

    private JsonNullable<Long> assigneeId;

    @NotBlank
    private JsonNullable<String> title;

    private JsonNullable<String> content;

    private JsonNullable<Set<String>> labelNames;

    @NotBlank
    private JsonNullable<String> status;
}
