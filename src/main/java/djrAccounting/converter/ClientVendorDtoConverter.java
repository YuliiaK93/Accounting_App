package djrAccounting.converter;

import djrAccounting.dto.ClientVendorDto;
import djrAccounting.service.ClientVendorService;

import org.springframework.core.convert.converter.Converter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ClientVendorDtoConverter implements Converter<String, ClientVendorDto> {

    ClientVendorService clientVendorService;

    public ClientVendorDtoConverter(@Lazy ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    @Override
    public ClientVendorDto convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return clientVendorService.findById(Long.parseLong(source));
    }
}
