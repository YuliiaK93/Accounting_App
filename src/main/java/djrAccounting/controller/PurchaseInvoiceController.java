package djrAccounting.controller;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.service.CompanyService;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {
    private final CompanyService companyService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    public PurchaseInvoiceController(CompanyService companyService, InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {
        InvoiceDto invoiceDto = invoiceService.findById(id);
        model.addAttribute("company", companyService.findById(invoiceDto.getCompany().getId()));
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(invoiceDto.getId()));

        return "invoice/invoice_print";
    }

    @GetMapping("/list")
    public String listPurchaseInvoice(Model model){

        model.addAttribute("invoices",invoiceService.getAllPurchaseInvoiceForCurrentCompany());

        return "invoice/purchase-invoice-list";

    }
}
