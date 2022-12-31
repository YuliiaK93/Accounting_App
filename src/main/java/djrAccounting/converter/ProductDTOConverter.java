package djrAccounting.converter;

import djrAccounting.dto.ProductDto;
import djrAccounting.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding


public class ProductDTOConverter implements Converter<String, ProductDto> {

    ProductService productService;

    public ProductDTOConverter(@Lazy ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDto convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return productService.findById(Long.parseLong(source));
    }
}
