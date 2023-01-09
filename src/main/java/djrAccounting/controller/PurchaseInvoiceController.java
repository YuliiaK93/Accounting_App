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
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;

    private final ProductService productService;
    private final SecurityService securityService;

    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService, SecurityService securityService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.securityService = securityService;
    }

    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        if (!invoiceDto.getCompany().equals(securityService.getLoggedInUser().getCompany())) return "redirect:/purchaseInvoices/list";

        model.addAttribute("company", securityService.getLoggedInUser().getCompany());
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(invoiceDto.getId()));

        return "invoice/invoice_print";
    }

    @GetMapping("/list")
    public String listPurchaseInvoice(Model model) {

        model.addAttribute("invoices", invoiceService.getAllPurchaseInvoiceForCurrentCompany());

        return "invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String createPurchaseInvoice(Model model) {
        model.addAttribute("newPurchaseInvoice", new InvoiceDto() {{
            setInvoiceNo(invoiceService.nextPurchaseInvoiceNo());
            setDate(LocalDate.now());
        }});
        model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());

        return "invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String createPurchaseInvoice(@Valid @ModelAttribute("newPurchaseInvoice") InvoiceDto invoiceDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());
            return "invoice/purchase-invoice-create";
        }
        invoiceService.save(invoiceDto);

        return "redirect:/purchaseInvoices/addInvoiceProduct/" + invoiceDto.getId();
    }

    @GetMapping({"/update/{id}", "/addInvoiceProduct/{id}"})
    public String updatePurchaseInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        if (!invoiceDto.getCompany().equals(securityService.getLoggedInUser().getCompany())) return "redirect:/purchaseInvoices/list";

        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("products", productService.listProductsBySelectedUserCompany());

        return "invoice/purchase-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String updatePurchaseInvoice(@Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto, BindingResult bindingResult, Model model, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());
            model.addAttribute("products", productService.listProductsBySelectedUserCompany());
            return "invoice/purchase-invoice-update";
        }

        invoiceProductService.save(invoiceProductDto, id);
        return "redirect:/purchaseInvoices/list";
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addProductToPurchaseInvoice(@PathVariable("invoiceId") Long invoiceId, @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", invoiceService.findById(invoiceId));
            model.addAttribute("vendors", clientVendorService.listVendorsBySelectedUserCompany());
            model.addAttribute("products", productService.listProductsBySelectedUserCompany());
            return "invoice/purchase-invoice-update";
        }
        invoiceProductService.save(invoiceProductDto, invoiceId);
        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable Long invoiceId, @PathVariable Long invoiceProductId, Model model){
        invoiceProductService.removeInvoiceProduct(invoiceProductId);
        return "redirect:/purchaseInvoices/update/"+invoiceId;
    }

    @GetMapping("/delete/{id}")
    public String deletePurchaseInvoice(@PathVariable("id") Long id){
        invoiceService.deletePurchaseInvoiceById(id);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approvePurchaseInvoice(@PathVariable("id") Long id, Model model){
        invoiceService.approvePurchaseInvoice(id);
     return "redirect:/purchaseInvoices/list";
    }
}
