package djrAccounting.controller;

import djrAccounting.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("products")


public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public String listProducts(Model model){
        model.addAttribute("products", productService.getAllProducts() );
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String getProductCreate(){
        //todo it is created to redirect after Client added
        return "";
    }



}
