package djrAccounting.service.implementation;

import djrAccounting.dto.CategoryDto;
import djrAccounting.entity.Category;
import djrAccounting.exception.CategoryNotFoundException;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CategoryRepository;
import djrAccounting.service.CategoryService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SecurityService securityService;
    private final ProductServiceImpl productService;
    private final MapperUtil mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, SecurityService securityService, ProductServiceImpl productService, MapperUtil mapper) {
        this.categoryRepository = categoryRepository;
        this.securityService = securityService;
        this.productService = productService;
        this.mapper = mapper;
    }

    @Override
    public CategoryDto findById(Long id) {
        return mapper.convert(
                categoryRepository.findById(id)
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found this id: " + id)),
                CategoryDto.class);
    }

    @Override
    public List<CategoryDto> listAllCategories() {
        List<CategoryDto> newList = categoryRepository.findByCompany_IdOrderByDescriptionAsc(securityService.getLoggedInUser()
                        .getCompany().getId()).stream().map(category -> mapper.convert(category, CategoryDto.class))
                .collect(Collectors.toList());

        newList.forEach(categoryDto -> categoryDto.setHasProduct(productService.productExistByCategory(categoryDto.getId())));

        return newList;
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {

        categoryDto.setCompany(securityService.getLoggedInUser().getCompany());

        Category category = mapper.convert(categoryDto, Category.class);

        return mapper.convert(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public boolean isCategoryDescriptionExist(String description){
        return categoryRepository.existsByDescriptionAndCompany_Id(description, securityService.getLoggedInUser().getCompany().getId() );
    }

    @Override
    public CategoryDto update(CategoryDto category) {

        categoryRepository.findById(category.getId()).orElseThrow(() -> new CategoryNotFoundException("Category not found this id: " + category.getId()));

        return mapper.convert(categoryRepository.save(mapper.convert(category, Category.class)), CategoryDto.class);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category with the given id not exist"));
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }
}