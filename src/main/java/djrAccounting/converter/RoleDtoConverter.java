package djrAccounting.converter;

import djrAccounting.dto.RoleDto;
import djrAccounting.service.RoleService;
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

    @Override
    public RoleDto convert(String source) {

        if (source.isBlank()) {
            return null;
        }

        return roleService.findById(Long.parseLong(source));
    }
}
