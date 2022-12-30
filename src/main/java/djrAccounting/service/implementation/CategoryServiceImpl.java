package djrAccounting.service.implementation;

import djrAccounting.dto.CategoryDto;
import djrAccounting.dto.ProductDto;
import djrAccounting.entity.Category;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CategoryRepository;
import djrAccounting.service.CategoryService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final SecurityService securityService;

    private final MapperUtil mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, SecurityService securityService, MapperUtil mapper) {
        this.categoryRepository = categoryRepository;
        this.securityService = securityService;
        this.mapper = mapper;
    }

    @Override
    public CategoryDto findById(Long id) {
        return mapper.convert(categoryRepository.findById(id).orElseThrow(), CategoryDto.class);
    }

    @Override
    public List<CategoryDto> listAllCategories() {
    return categoryRepository.findByCompany_IdOrderByDescriptionAsc(securityService.getLoggedInUser()
            .getCompany().getId()).stream().map(category -> mapper.convert(category, CategoryDto.class))
            .collect(Collectors.toList());
    }


}

