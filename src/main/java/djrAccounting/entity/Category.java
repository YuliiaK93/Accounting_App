package djrAccounting.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "categories")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Category extends BaseEntity {

    private String description;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", updatable = false)
    private Company company;
}