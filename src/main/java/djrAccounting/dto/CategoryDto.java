package djrAccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Size should be between 2 and 50")
    private String description;

    private CompanyDto company;
}
