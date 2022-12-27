package djrAccounting.dto;

import djrAccounting.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {

    private Long id;

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Size should be between 2 and 50")
    private String name;
    private int quantityInStock;

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Should be minimum 1")
    private int lowLimitAlert;

    @NotNull(message = "Required field")
    private ProductUnit productUnit;

    @NotNull(message = "Required field")
    private CategoryDto category;
}
