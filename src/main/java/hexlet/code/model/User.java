package hexlet.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @ToString.Include
    private String firstName;

    @ToString.Include
    private String lastName;

    @ToString.Include
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @Size(min = 3)
    private String password;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updatedAt;
}
