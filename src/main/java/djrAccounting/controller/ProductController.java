package djrAccounting.controller;

import djrAccounting.dto.ProductDto;
import djrAccounting.enums.ProductUnit;
import djrAccounting.service.CategoryService;
import djrAccounting.service.ProductService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")


public class ProductController {

    private final ProductService productService;

    private final CategoryService categoryService;
    private final SecurityService securityService;

    public ProductController(ProductService productService, CategoryService categoryService, SecurityService securityService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/product/product-list";
    }

    @GetMapping("/update/{id}")
    public String updateProduct(Model model, @PathVariable Long id){

        ProductDto productDto = productService.findById(id);
        if (!productDto.getCategory().getCompany().equals(securityService.getLoggedInUser().getCompany())) return "redirect:/products/list";

        model.addAttribute("product", productDto);
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", ProductUnit.values());
        return "/product/product-update";
    }
@PostMapping("/update/{id}")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.listAllCategories());
            model.addAttribute("productUnits", ProductUnit.values());
            return "/product/product-update";
        }
        productService.update(productDto);
        return "redirect:/products/list";
}
    @GetMapping("/create")
    public String createProduct(Model model){
        model.addAttribute("newProduct", new ProductDto());
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", ProductUnit.values());
        return "/product/product-create";
    }
@PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("newProduct") ProductDto productDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.listAllCategories());
            model.addAttribute("productUnits", ProductUnit.values());
            return "/product/product-create";
        }
        productService.save(productDto);
        return "redirect:/products/list";
}
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProductById(id);

        return "redirect:/products/list";
    }

}
