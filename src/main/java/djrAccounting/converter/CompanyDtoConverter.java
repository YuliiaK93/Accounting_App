package djrAccounting.converter;

import djrAccounting.dto.CompanyDto;
import djrAccounting.service.CompanyService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyDtoConverter implements Converter<String, CompanyDto> {

    private final CompanyService companyService;

    public CompanyDtoConverter(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public CompanyDto convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return companyService.findById(Long.parseLong(source));
    }
}
