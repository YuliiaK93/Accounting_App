package djrAccounting.entity;

import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "invoices")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
public class Invoice extends BaseEntity {

    @Column(updatable = false)
    private String invoiceNo;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private InvoiceType invoiceType;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "client_vendor_id")
    private ClientVendor clientVendor;

    @ManyToOne
    @JoinColumn(name = "company_id", updatable = false)
    private Company company;

    @OneToMany(mappedBy = "invoice")
    private List<InvoiceProduct> invoiceProducts;
}