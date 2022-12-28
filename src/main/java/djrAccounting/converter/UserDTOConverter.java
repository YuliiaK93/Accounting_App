package djrAccounting.converter;

import djrAccounting.dto.UserDto;
import djrAccounting.service.UserService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class UserDTOConverter implements Converter<String, UserDto> {

    UserService userService;

    public UserDTOConverter(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDto convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return userService.findById(Long.parseLong(source));
    }
}