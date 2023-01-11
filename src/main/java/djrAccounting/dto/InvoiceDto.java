package djrAccounting.dto;

import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvoiceDto {
    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private InvoiceType invoiceType;
    private List<InvoiceProductDto> invoiceProducts;

    @NotNull
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    @NotNull(message = "Required field")
    @Valid
    private ClientVendorDto clientVendor;

    private CompanyDto company;
    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;
}
