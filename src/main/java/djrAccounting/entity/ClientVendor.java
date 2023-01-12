package djrAccounting.entity;

import djrAccounting.enums.ClientVendorType;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "clients_vendors")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientVendor extends BaseEntity {

    @Column(unique = true)
    private String clientVendorName;
    private String phone;
    private String website;

    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "company_id", updatable = false)
    private Company company;
}
