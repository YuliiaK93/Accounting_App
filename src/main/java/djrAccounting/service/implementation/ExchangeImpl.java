package djrAccounting.service.implementation;

import djrAccounting.client.ExchangeClient;
import djrAccounting.dto.ExchangeRatesDto;
import org.springframework.stereotype.Service;

@Service
public class ExchangeImpl {

    private final ExchangeClient exchangeClient;

    public ExchangeImpl(ExchangeClient exchangeClient) {
        this.exchangeClient = exchangeClient;
    }

    public ExchangeRatesDto getExchangeRates() {
        return exchangeClient.getExchangeRates();
    }
}
