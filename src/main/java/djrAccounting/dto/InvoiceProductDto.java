package djrAccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceProductDto {

    private Long id;

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Should be more than 0")
    private int quantity;

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Should be more than 0")
    private BigDecimal price;

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Should be more than 0")
    private int tax;
    private BigDecimal profitLoss;
    private int remainingQuantity;
    private InvoiceDto invoice;

    @NotNull
    private ProductDto product;

    private BigDecimal total;

    public BigDecimal getTotal() {
        BigDecimal beforeTax = BigDecimal.valueOf(quantity).multiply(price);
        BigDecimal taxValue = BigDecimal.valueOf(tax).multiply(beforeTax).divide(BigDecimal.valueOf(100L));
        return beforeTax.add(taxValue);
    }
}
