package hexlet.code.component;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabelResolver {

    @Autowired
    private LabelRepository labelRepository;

    public Label fromName(String name) {
        return labelRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Label with name " + name + " not found"));

    }
}
