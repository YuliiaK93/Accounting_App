package djrAccounting.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(unique = true)
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;


    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id", updatable = false)
    private Company company;

    public User(Long id, Boolean isDeleted, LocalDateTime insertDateTime, Long insertUserId, LocalDateTime lastUpdateDateTime, Long lastUpdateUserId, String username, String password, String firstname, String lastname, String phone, boolean enabled, Role role, Company company) {
        super(id, isDeleted, insertDateTime, insertUserId, lastUpdateDateTime, lastUpdateUserId);
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.enabled = enabled;
        this.role = role;
        this.company = company;
    }
}