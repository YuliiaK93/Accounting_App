package djrAccounting.controller;

import djrAccounting.dto.CategoryDto;
import djrAccounting.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
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

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id)  {
        categoryService.deleteCategoryById(id);

        return "redirect:/categories/list";
    }


}
