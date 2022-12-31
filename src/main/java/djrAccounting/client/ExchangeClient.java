package djrAccounting.client;

import djrAccounting.dto.currency.ExchangeDto;
import djrAccounting.dto.currency.UsdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/usd.json", name = "EXCHANGE-RATES")
public interface ExchangeClient {
    @GetMapping
    ExchangeDto getExchangeRates();
}
