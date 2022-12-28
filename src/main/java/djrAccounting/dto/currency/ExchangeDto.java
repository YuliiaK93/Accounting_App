package djrAccounting.dto.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExchangeDto {


    private LocalDate date;

    @JsonProperty("usd")
    private UsdDto usdDto;
}
