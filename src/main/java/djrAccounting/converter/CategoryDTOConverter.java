package djrAccounting.converter;

import djrAccounting.dto.CategoryDto;
import djrAccounting.service.CategoryService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class CategoryDTOConverter implements Converter<String, CategoryDto> {

    CategoryService categoryService;

    public CategoryDTOConverter(@Lazy CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDto convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return categoryService.findById(Long.parseLong(source));
    }
}
