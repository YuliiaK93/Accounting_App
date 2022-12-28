package djrAccounting.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class ExchangeRatesDto {

    private BigDecimal euro;
    private BigDecimal britishPound;
    private BigDecimal canadianDollar;
    private BigDecimal japaneseYen;
    private BigDecimal indianRupee;






}
