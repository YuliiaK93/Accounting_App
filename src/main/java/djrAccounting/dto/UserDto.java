package djrAccounting.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Valid
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "Username is required field.")
    @Email(message = "A user with this email already exists. Please try with different email.")
    private String username;

    @NotBlank(message = "Password is required field")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}", message = "Password should be at least 4 characters long and needs to contain 1 capital letter,1 small letter and 1 special character or number.")
    private String password;

    @NotNull(message = "Passwords should match.")
    private String confirmPassword;

    @NotBlank(message = "First Name is required field.")
    @Size(max = 50, min = 2, message = "First Name must be between 2 and 50 characters long.")
    private String firstname;

    @NotBlank(message = "Last Name is required field.")
    @Size(max = 50, min = 2, message = "Last Name must be between 2 and 50 characters long.")
    private String lastname;

    @NotBlank
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;
    private boolean enabled = true;

    @NotNull(message = "Please select a Role")
    private RoleDto role;

    @NotNull(message = "Please select a Customer.")
    private CompanyDto company;

    Boolean isOnlyAdmin;

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getOnlyAdmin() {
        return isOnlyAdmin;
    }

    public void setOnlyAdmin(Boolean onlyAdmin) {
        isOnlyAdmin = onlyAdmin;
    }
}