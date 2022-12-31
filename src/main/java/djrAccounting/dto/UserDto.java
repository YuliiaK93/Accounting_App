package djrAccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Email is required field. A user with this email already exists. Please try with different email.")
    @Email
    private String username;

    @NotBlank(message = "Password should be at least 4 characters long and needs to contain 1 capital letter, 1 small letter and 1 special character or number.")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}")
    private String password;

    @NotNull
    @NotBlank(message = "Passwords should match.")
    private String confirmPassWord;

    @NotBlank(message = "First Name is required field. First Name must be between 2 and 50 characters long.")
    @Size(max = 50, min = 2)
    private String firstname;

    @NotBlank(message = "Last Name is required field. First Name must be between 2 and 50 characters long.")
    @Size(max = 50, min = 2)
    private String lastname;

    @NotBlank(message = "Phone Number is required field and may be in any valid phone number format.")
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
    private String phone;
    private boolean enabled;

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    @NotNull(message = "Please select a Role")
    private RoleDto role;

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    @NotNull(message = "Please select a Customer.")
    private CompanyDto company;


    public boolean isOnlyAdmin() {
        return true; //TODO will be implemented by @Yuliia after security context
    }

    public void setOnlyAdmin(boolean onlyAdmin) {
        isOnlyAdmin = onlyAdmin;
    }

    private boolean isOnlyAdmin;
}
