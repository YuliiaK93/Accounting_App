package djrAccounting.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "addresses")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Address extends BaseEntity {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}