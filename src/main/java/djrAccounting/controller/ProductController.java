package djrAccounting.controller;

import djrAccounting.dto.ProductDto;
import djrAccounting.enums.ProductUnit;
import djrAccounting.service.CategoryService;
import djrAccounting.service.ProductService;
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



    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/product/product-list";
    }

    @GetMapping("/update/{id}")
    public String updateProduct(Model model, @PathVariable Long id){
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", ProductUnit.values());
        return "/product/product-update";
    }
@PostMapping("/update/{id}")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, Model model){

        if (productService.isNameAlreadyInUse(productDto.getName()) && productService.isNameNotPrevious(productDto.getId(), productDto.getName())) {

            bindingResult.rejectValue("name", "", "You already have a product with same name");

        }

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

        if (productService.isNameAlreadyInUse(productDto.getName())){

            bindingResult.rejectValue("name","","You already have a product with same name");
        }

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
