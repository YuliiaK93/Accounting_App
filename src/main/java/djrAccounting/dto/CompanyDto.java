package djrAccounting.dto;

import djrAccounting.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyDto {

    private Long id;

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Size should be between 2 and 50")
    private String title;

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Size should be between 2 and 50")
    private String phone;

    @NotBlank(message = "Required field")
    private String website;

    private CompanyStatus companyStatus;

    @Valid
    private AddressDto address;
}
