package djrAccounting.controller;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.service.ClientVendorService;
import djrAccounting.service.CompanyService;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {
    private final CompanyService companyService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;

    public PurchaseInvoiceController(CompanyService companyService, InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
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

    @GetMapping("/create")
    public String createPurchaseInvoice(Model model){
        InvoiceDto invoiceDto= new InvoiceDto();
        model.addAttribute("newPurchaseInvoice", invoiceDto);
        invoiceDto.setInvoiceNo(invoiceService.nextPurchaseInvoiceNo());
        invoiceDto.setDate(LocalDate.now());
        model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());
        return "invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String createPurchaseInvoice(@Valid @ModelAttribute("newPurchaseInvoice") InvoiceDto invoiceDto, BindingResult bindingResult, Model model){
        model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());
        if(bindingResult.hasErrors()){
            model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());
            return "invoice/purchase-invoice-create";
        }
        invoiceService.save(invoiceDto);
        return "redirect:/purchaseInvoices/update/"+invoiceDto.getId();
    }
}
