package djrAccounting.dto;

import djrAccounting.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientVendorDto {

    private Long id;

    @NotBlank(message = "Company Name is a required field.")
    @Size(min = 2, max = 50, message = "Company Name should have 2-50 characters long.")
    private String clientVendorName;
    @NotBlank(message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;
    @NotBlank
    @Pattern(regexp = "^http(s{0,1})://[a-zA-Z0-9/\\-\\.]+.([A-Za-z/]{2,5})[a-zA-Z0-9/\\&\\?\\=\\-\\.\\~\\%]*", message = "Website should have a valid format.")
    private String website;

    @NotNull(message = "Please select type.")
    private ClientVendorType clientVendorType;

    @NotBlank(message = "Address is required field.")
    @Size(min = 2, max = 100, message = "Address should have 2-100 characters long")
    private AddressDto address;

    private CompanyDto company;
}
