package hexlet.code.dto.label;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelUpdateDTO {

    @Column(unique = true)
    @Size(min = 3, max = 1000)
    private String name;
}
