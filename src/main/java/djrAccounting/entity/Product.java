package djrAccounting.entity;

import djrAccounting.enums.ProductUnit;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "products")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    private String name;
    private int quantityInStock;
    private int lowLimitAlert;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}