package djrAccounting.enums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum Role {
    ROOT_USER("Root User", 1L),
    ADMIN("Admin", 2L),
    MANAGER("Manager", 3L),
    EMPLOYEE("Employee", 4L);

    private final String value;
    private final long id;
}
