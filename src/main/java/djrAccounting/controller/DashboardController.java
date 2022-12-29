package djrAccounting.controller;

import djrAccounting.service.InvoiceService;
import djrAccounting.service.dashboard.FinancialSummaryService;
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
    private final FinancialSummaryService financialSummaryService;

    public DashboardController(ExchangeImpl exchange, InvoiceService invoiceService, FinancialSummaryService financialSummaryService) {
        this.exchange = exchange;
        this.invoiceService = invoiceService;
        this.financialSummaryService = financialSummaryService;
    }


    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("exchangeRates", exchange.getExchangeRates());
        model.addAttribute("invoices", invoiceService.getLast3ApprovedInvoicesForCurrentUserCompany());
        model.addAttribute("summaryNumbers", financialSummaryService.financialSummaryForCurrentCompany());

        return "dashboard";
    }
}
