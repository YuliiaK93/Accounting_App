package djrAccounting.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "invoice_products")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
public class InvoiceProduct extends BaseEntity {

    @Column(updatable = false)
    private int quantity;
    @Column(updatable = false)
    private BigDecimal price;
    @Column(updatable = false)
    private int tax;
    private BigDecimal profitLoss;
    private int remainingQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", updatable = false)
    private Invoice invoice;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}