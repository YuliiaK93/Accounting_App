package djrAccounting.controller;

import djrAccounting.service.InvoiceService;
import djrAccounting.service.implementation.ExchangeImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ExchangeImpl exchange;
    private final InvoiceService invoiceService;

    public DashboardController(ExchangeImpl exchange, InvoiceService invoiceService) {
        this.exchange = exchange;
        this.invoiceService = invoiceService;
    }


    @GetMapping
    public String showEuroRate(Model model) {
        model.addAttribute("exchangeRates", exchange.getExchangeRates());

        return "dashboard";
    }

    @GetMapping
    public String lastTransactions(Model model) {

        model.addAttribute("invoices", invoiceService.getLast3ApprovedInvoicesForCurrentUserCompany());

        return "dashboard";
    }
}
