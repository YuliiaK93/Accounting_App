package djrAccounting.controller;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.dto.ProductDto;
import djrAccounting.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final CompanyService companyService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;

    public SalesInvoiceController(CompanyService companyService, InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public String listInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.findSalesInvoicesByCurrentUserCompany());
        return "invoice/sales-invoice-list";
    }

    @GetMapping("/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {
        InvoiceDto invoiceDto = invoiceService.findById(id);
        model.addAttribute("company", companyService.findById(invoiceDto.getCompany().getId()));
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(invoiceDto.getId()));
        return "invoice/invoice_print";
    }

    @GetMapping("/create")
    public String createSalesInvoicePage(Model model) {
        InvoiceDto invoiceDto = new InvoiceDto();
        model.addAttribute("newSalesInvoice", invoiceDto);
        invoiceDto.setInvoiceNo(invoiceService.nextSalesInvoiceNo());
        invoiceDto.setDate(LocalDate.now());
        model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());

        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String createSalesInvoice(@Valid @ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto, BindingResult bindingResult, Model model) {
        model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());

        if (bindingResult.hasErrors()) {
            model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());
            return "invoice/sales-invoice-create";
        }

        invoiceService.save(invoiceDto);
        return "redirect:/salesInvoices/update/"+invoiceDto.getId();
    }

    @GetMapping("/update/{id}")
    public String getUpdateSalesInvoice(@PathVariable("id") Long id,Model model){
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());
        InvoiceProductDto invoiceProductDto= new InvoiceProductDto();
        model.addAttribute("newInvoiceProduct",invoiceProductDto);
        model.addAttribute("products",productService.listProductsBySelectedUserCompany());
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(id));

        return "invoice/sales-invoice-update";
    }

    @PostMapping("/addInvoiceProduct/{id}")
    public String addInvoiceProduct(@PathVariable("id") Long id,@ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto,Model model){
      //  model.addAttribute()


        //return "redirect:/salesInvoices/update/"+invoiceProductDto.;
        return "";
    }


    @PostMapping("/update/{id}")
    public String updateSalesInvoice(@PathVariable("id") Long id, InvoiceDto invoiceDto) {
        //todo @mehmet will implement update
        return "redirect:/salesInvoices/list";
    }
}
