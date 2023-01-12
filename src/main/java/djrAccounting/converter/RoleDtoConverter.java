package djrAccounting.converter;

import djrAccounting.dto.RoleDto;
import djrAccounting.service.RoleService;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class RoleDtoConverter implements Converter<String, RoleDto> {

    RoleService roleService;

    public RoleDtoConverter(@Lazy RoleService roleService) {
        this.roleService = roleService;
    }

    @SneakyThrows
    @Override
    public RoleDto convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return roleService.findById(Long.parseLong(source));
    }
}
