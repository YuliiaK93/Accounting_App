package djrAccounting.converter;


import djrAccounting.dto.InvoiceDto;
import djrAccounting.service.InvoiceService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;
@Component
@ConfigurationPropertiesBinding
public class InvoiceDTOConverter implements Converter<String, InvoiceDto> {

    InvoiceService invoiceService;

    public InvoiceDTOConverter(@Lazy InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceDto convert(String source) {
        if(source == null || source.isBlank()){
            return null;
        }

       return invoiceService.findById(Long.parseLong(source));
    }
}
