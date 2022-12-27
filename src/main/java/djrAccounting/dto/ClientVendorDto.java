package djrAccounting.dto;

import djrAccounting.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientVendorDto {

    private Long id;

    @NotBlank(message = "Required field")
    private String clientVendorName;
    @NotBlank(message = "Required field")
    private String phone;
    @NotBlank(message = "Required field")
    private String website;

    @NotNull(message = "Required field")
    private ClientVendorType clientVendorType;

    @NotNull(message = "Required field")
    private AddressDto address;

    private CompanyDto company;
}
