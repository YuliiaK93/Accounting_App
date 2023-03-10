package djrAccounting.controller;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.dto.InvoiceProductDto;
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

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final SecurityService securityService;

    public SalesInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService, SecurityService securityService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String listInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.findSalesInvoicesByCurrentUserCompany());
        return "invoice/sales-invoice-list";
    }

    @GetMapping("/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        if (!invoiceDto.getCompany().equals(securityService.getLoggedInUser().getCompany()))
            return "redirect:/salesInvoices/list";

        model.addAttribute("company", securityService.getLoggedInUser().getCompany());
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(invoiceDto.getId()));
        return "invoice/invoice-print";
    }

    @GetMapping("/approve/{id}")
    public String approveInvoiceGet(@PathVariable("id") Long id) {
        invoiceService.approveInvoiceById(id);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) {
        invoiceService.deleteInvoiceById(id);
        return "redirect:/salesInvoices/list";
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

        return "redirect:/salesInvoices/update/" + invoiceService.save(invoiceDto).getId();
    }

    @GetMapping("/update/{id}")
    public String getUpdateSalesInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        if (!invoiceDto.getCompany().equals(securityService.getLoggedInUser().getCompany()))
            return "redirect:/salesInvoices/list";

        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());
        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        model.addAttribute("newInvoiceProduct", invoiceProductDto);
        model.addAttribute("products", productService.listProductsBySelectedUserCompany());
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(id));

        return "invoice/sales-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String editSalesInvoice(@Valid @ModelAttribute("invoice") InvoiceDto invoice, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());
            model.addAttribute("products", productService.listProductsBySelectedUserCompany());

            return "invoice/sales-invoice-update";
        }

        invoiceService.update(invoice);

        return "redirect:/salesInvoices/list";
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addInvoiceProduct(@PathVariable("invoiceId") Long id, @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());
            model.addAttribute("products", productService.listProductsBySelectedUserCompany());
            model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(id));
            return "invoice/sales-invoice-update";
        }

        boolean isStockEnough = productService.isStockEnough(invoiceProductDto);

        if (!isStockEnough) {
            bindingResult.rejectValue("quantity", " ", "Not enough " + invoiceProductDto.getProduct().getName() + " quantity to sell.");
            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("clients", clientVendorService.listClientsBySelectedUserCompany());
            model.addAttribute("products", productService.listProductsBySelectedUserCompany());
            model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(id));
            return "invoice/sales-invoice-update";
        }

        invoiceProductService.save(invoiceProductDto, id);

        return "redirect:/salesInvoices/update/" + id;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProuductId}")
    public String removeProductFromTheInvoice(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProuductId") Long invoiceProductId) {
        invoiceProductService.deleteInvoiceProductById(invoiceProductId);

        return "redirect:/salesInvoices/update/" + invoiceId;
    }
}
