package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Optional<Task> findByName(String name);
    boolean existsByAssigneeId(Long assigneeId);
    boolean existsByTaskStatusId(Long taskStatusId);
    boolean existsByLabelsId(Long labelId);
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.labels WHERE t.name = :name")
    Optional<Task> findByNameWithLabels(String name);
}
