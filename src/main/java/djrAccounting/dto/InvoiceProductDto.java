package djrAccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;

import javax.persistence.FetchType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceProductDto {

    private Long id;

    @NotNull(message =  "Quantity is a required field." )
    @Min(value = 1, message = "Should be more than 0")
    @Max(value = 100, message = "Maximum order count is 100" )
    private int quantity;

    @NotNull(message = "Price is a required field.")
    @Min(value = 1, message = "Price should be at least $1")
    private BigDecimal price;

    @NotNull(message = "Tax is a required field.")
    @Min(value = 5, message = "Tax should be between 5% and 20%")
    @Max(value =20,message = "Tax should be between 5% and 20%")
    private int tax;
    private BigDecimal profitLoss;
    private int remainingQuantity;
    private InvoiceDto invoice;

    @Valid
    @NotNull(message = "Product is a required field")
    private ProductDto product;

    private BigDecimal total;

    public BigDecimal getTotal() {
        BigDecimal beforeTax = BigDecimal.valueOf(quantity).multiply(price);
        BigDecimal taxValue = BigDecimal.valueOf(tax).multiply(beforeTax).divide(BigDecimal.valueOf(100L));
        return beforeTax.add(taxValue);
    }
}
