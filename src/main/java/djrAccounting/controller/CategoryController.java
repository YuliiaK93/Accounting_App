package djrAccounting.controller;

import djrAccounting.dto.CategoryDto;
import djrAccounting.service.CategoryService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final SecurityService securityService;


    public CategoryController(CategoryService categoryService, SecurityService securityService) {
        this.categoryService = categoryService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.listAllCategories());
        return "category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model){
        model.addAttribute("newCategory", new CategoryDto());
        return "category/category-create";
    }

    @PostMapping("/create")
    public String insertCategory(@Valid @ModelAttribute("newCategory")CategoryDto category, BindingResult bindingResult){
        if(categoryService.isCategoryDescriptionExist(category.getDescription())){
            bindingResult.rejectValue("description", " ",
                    "You already have a category name with this description");
        }

        if (bindingResult.hasErrors()) {
            return "category/category-create";
        }

        categoryService.save(category);
        return "redirect:/categories/list";
    }

    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model){

        CategoryDto categoryDto = categoryService.findById(id);
        if (!categoryDto.getCompany().equals(securityService.getLoggedInUser().getCompany())) return "redirect:/categories/list";

        model.addAttribute("category", categoryDto);
        return "category/category-update";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDto category, BindingResult bindingResult){
        if(categoryService.isCategoryDescriptionExist(category.getDescription())){
            bindingResult.rejectValue("description", " ",
                    "This category already has product/products! Make sure the new description that will be provided is proper.");
        }

        if (bindingResult.hasErrors()) {
            return "category/category-update";
        }
        categoryService.update(category);
        return "redirect:/categories/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id){

        CategoryDto categoryDto = categoryService.findById(id);
        if (!categoryDto.getCompany().equals(securityService.getLoggedInUser().getCompany())) return "redirect:/categories/list";

        categoryService.deleteCategoryById(id);
        return "redirect:/categories/list";
    }
}
