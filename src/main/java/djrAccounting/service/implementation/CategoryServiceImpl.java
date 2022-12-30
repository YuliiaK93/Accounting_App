package djrAccounting.service.implementation;

import djrAccounting.dto.CategoryDto;
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
        return mapper.convert(categoryRepository.findById(id).orElseThrow(), CategoryDto.class);
    }

    @Override
    public List<CategoryDto> listAllCategories() {
        List<CategoryDto> newList = categoryRepository.findByCompany_IdOrderByDescriptionAsc(securityService.getLoggedInUser()
            .getCompany().getId()).stream().map(category -> mapper.convert(category, CategoryDto.class))
            .collect(Collectors.toList());

        newList.forEach(categoryDto -> {
            if (productService.productExistByCategory(categoryDto.getId())){
                categoryDto.setHasProduct(true);
            } else {
                categoryDto.setHasProduct(false);
            }

        });

        return newList;
    }

}
