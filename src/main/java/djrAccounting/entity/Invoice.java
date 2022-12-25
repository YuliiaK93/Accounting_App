package djrAccounting.entity;

import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "invoices")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
public class Invoice extends BaseEntity {

    String invoiceNo;

    @Enumerated(EnumType.STRING)
    InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    InvoiceType invoiceType;
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "client_vendor_id")
    ClientVendor clientVendor;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;
}