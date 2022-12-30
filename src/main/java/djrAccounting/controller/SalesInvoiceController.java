package djrAccounting.controller;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.service.CompanyService;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SalesInvoiceController {

    private final CompanyService companyService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    public SalesInvoiceController(CompanyService companyService, InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }


    @GetMapping("/salesInvoices/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        model.addAttribute("company", companyService.findById(invoiceDto.getCompany().getId()));
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(invoiceDto.getId()));

        return "invoice/invoice_print";
    }

    @GetMapping("/purchaseInvoices/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        model.addAttribute("company", companyService.findById(invoiceDto.getCompany().getId()));
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(invoiceDto.getId()));

        return "invoice/invoice_print";
    }
}
