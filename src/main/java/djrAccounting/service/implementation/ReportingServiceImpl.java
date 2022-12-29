package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.enums.InvoiceType;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.ReportingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final InvoiceProductService invoiceProductService;

    public ReportingServiceImpl(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public List<InvoiceProductDto> getAllInvoiceProductsThatApprovedFroCurrentCompany() {

        return invoiceProductService.getAllByInvoiceStatusApprovedForCurrentCompany();
    }

    @Override
    public Map<String, BigDecimal> getAllProfitLossPerMonth() {

        Map<String, BigDecimal> map = new LinkedHashMap<>();

        invoiceProductService.getAllByInvoiceStatusApprovedForCurrentCompany().forEach(invoiceProductDto -> {

            LocalDate date = invoiceProductDto.getInvoice().getDate();
            String key = date.getYear() + " " + date.getMonth();
            BigDecimal priceWithTax = invoiceProductDto.getPrice()
                    .add(invoiceProductDto.getPrice()
                            .multiply(BigDecimal.valueOf(invoiceProductDto.getTax()))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                    .multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity()));

            if (invoiceProductDto.getInvoice().getInvoiceType().equals(InvoiceType.SALES)){

                map.put(key, map.getOrDefault(key, BigDecimal.ZERO).add(priceWithTax));

            }else {

                map.put(key, map.getOrDefault(key, BigDecimal.ZERO).subtract(priceWithTax));
            }
        });

        return map;
    }

//    @Override
//    public Map<String, BigDecimal> getAllProfitLossPerMonth() {
//
//        Map<String, BigDecimal> map = new LinkedHashMap<>();
//
//        invoiceProductService.getAllByInvoiceStatusApprovedForCurrentCompany().forEach(invoiceProductDto -> {
//
//                    LocalDate date = invoiceProductDto.getInvoice().getDate();
//                    String key = date.getYear() + " " + date.getMonth();
//
//                    map.put(key, map.getOrDefault(key, BigDecimal.ZERO).add(invoiceProductDto.getProfitLoss()));
//                });
//
//        return map;
//    }
}
