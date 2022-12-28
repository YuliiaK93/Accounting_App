package djrAccounting.dto.currency;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class UsdDto {

    @JsonAlias("eur")
    private BigDecimal euro;

    @JsonAlias("gbp")
    private BigDecimal britishPound;

    @JsonAlias("cad")
    private BigDecimal canadianDollar;

    @JsonAlias("jpy")
    private BigDecimal japaneseYen;

    @JsonAlias("inr")
    private BigDecimal indianRupee;


}
