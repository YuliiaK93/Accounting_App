package djrAccounting.controller;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.service.CompanyService;
import djrAccounting.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class InvoiceController {

    private final CompanyService companyService;
    private final InvoiceService invoiceService;

    public InvoiceController(CompanyService companyService, InvoiceService invoiceService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
    }


    @GetMapping("/salesInvoices/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        model.addAttribute("company", companyService.findById(invoiceDto.getCompany().getId()));
        model.addAttribute("invoice", invoiceService.findById(id));

        return "invoice_print";
    }

    @GetMapping("/purchaseInvoices/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        model.addAttribute("company", companyService.findById(invoiceDto.getCompany().getId()));
        model.addAttribute("invoice", invoiceService.findById(id));

        return "invoice_print";
    }
}
