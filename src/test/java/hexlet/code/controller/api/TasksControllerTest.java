package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;

    private TaskStatus testTaskStatus;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity()).build();

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setTaskStatus(testTaskStatus);
    }

    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);

        var response = mockMvc.perform(get("/api/tasks").with(jwt())).andExpect(status().isOk()).andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskDTO> taskDTOS = om.readValue(body, new TypeReference<>() {
        });

        var actual = taskDTOS.stream().map(taskMapper::map).toList();
        var expected = taskRepository.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testCreate() throws Exception {
        var taskCreateDto = new TaskCreateDTO();
        taskCreateDto.setTitle("Test title");
        taskCreateDto.setStatus(testTaskStatus.getSlug());

        var request = post("/api/tasks").with(jwt()).contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(taskCreateDto));
        mockMvc.perform(request).andExpect(status().isCreated());

        var task = taskRepository.findByName(taskCreateDto.getTitle()).orElse(null);
        assertNotNull(task);
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(taskCreateDto.getStatus());
    }

    @Test
    public void testShow() throws Exception {
        taskRepository.save(testTask);

        var request = get("/api/tasks/" + testTask.getId()).with(jwt());
        var response = mockMvc.perform(request).andExpect(status().isOk()).andReturn().getResponse();
        var body = response.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        taskRepository.save(testTask);

        var data = new HashMap<>();
        data.put("title", "new title");
        data.put("content", "new content");

        var request = put("/api/tasks/" + testTask.getId()).with(jwt()).contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isOk());

        testTask = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(testTask.getName()).isEqualTo(data.get("title"));
        assertThat(testTask.getDescription()).isEqualTo(data.get("content"));
    }

    @Test
    public void testUpdateFailed() throws Exception {
        taskRepository.save(testTask);

        var data = new HashMap<>();
        data.put("title", "new title");
        data.put("content", "new content");

        var request = put("/api/tasks/" + testTask.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isUnauthorized());

        var actualTask = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(actualTask.getDescription()).isEqualTo(testTask.getDescription());
        assertThat(actualTask.getName()).isEqualTo(testTask.getName());
    }

    @Test
    public void testDestroy() throws Exception {
        taskRepository.save(testTask);

        var request = delete("/api/tasks/" + testTask.getId()).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());

        assertFalse(taskRepository.existsById(testTask.getId()));
    }

    @Test
    public void testDestroyFailed() throws Exception {
        taskRepository.save(testTask);

        var request = delete("/api/tasks/" + testTask.getId());
        mockMvc.perform(request).andExpect(status().isUnauthorized());

        assertTrue(taskRepository.existsById(testTask.getId()));
    }
}
