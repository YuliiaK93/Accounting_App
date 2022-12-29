package djrAccounting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportingController {

    @GetMapping("/profitLossData")
    public String listProfitLoss(Model model) {

        model.addAttribute("monthlyProfitLossDataMap");

        return "report/profit-loss-report";
    }

    @GetMapping("/stockData")
    public String listStockData(Model model) {

        model.addAttribute("invoiceProducts");

        return "report/stock-report";
    }
}
