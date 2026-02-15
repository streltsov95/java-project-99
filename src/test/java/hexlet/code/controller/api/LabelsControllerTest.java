package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelsControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity()).build();

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        labelRepository.save(testLabel);

        var response = mockMvc.perform(get("/api/labels").with(jwt())).andExpect(status().isOk()).andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<LabelDTO> labelDTOs = om.readValue(body, new TypeReference<>() {
        });

        var actual = labelDTOs.stream().map(labelMapper::map).toList();
        var expected = labelRepository.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testShow() throws Exception {
        labelRepository.save(testLabel);

        var request = get("/api/labels/" + testLabel.getId()).with(jwt());
        var response = mockMvc.perform(request).andExpect(status().isOk()).andReturn().getResponse();
        var body = response.getContentAsString();

        assertThatJson(body).and((v -> v.node("name").isEqualTo(testLabel.getName())));
    }

    @Test
    public void testCreate() throws Exception {
        var labelCreateDto = new LabelCreateDTO();
        labelCreateDto.setName("Test label name");

        var request = post("/api/labels").with(jwt()).contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(labelCreateDto));
        mockMvc.perform(request).andExpect(status().isCreated());

        var label = labelRepository.findByName(labelCreateDto.getName()).orElse(null);

        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(labelCreateDto.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        labelRepository.save(testLabel);

        var data = new HashMap<>();
        data.put("name", "Updated test label name");

        var request = put("/api/labels/" + testLabel.getId()).with(jwt()).contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isOk());

        testLabel = labelRepository.findById(testLabel.getId()).orElseThrow();

        assertThat(testLabel.getName()).isEqualTo(data.get("name"));
    }

    @Test
    public void testDestroy() throws Exception {
        labelRepository.save(testLabel);

        var request = delete("/api/labels/" + testLabel.getId()).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());

        assertFalse(labelRepository.existsById(testLabel.getId()));
    }

    @Test
    public void testDestroyFailed() throws Exception {
        var testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        var testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setTaskStatus(testTaskStatus);
        testTask.addLabel(testLabel);
        taskRepository.save(testTask);

//        labelRepository.save(testLabel);

        var request = delete("/api/labels/" + testLabel.getId()).with(jwt());
        mockMvc.perform(request).andExpect(status().isConflict());

        assertTrue(labelRepository.existsById(testLabel.getId()));
    }
}
