package djrAccounting.controller;

import djrAccounting.service.implementation.ExchangeImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ExchangeImpl exchange;

    public DashboardController(ExchangeImpl exchange) {
        this.exchange = exchange;
    }


    @GetMapping
    public String showEuroRate(Model model) {
        model.addAttribute("exchangeRates", exchange.getExchangeRates());

        return "dashboard";
    }

}
