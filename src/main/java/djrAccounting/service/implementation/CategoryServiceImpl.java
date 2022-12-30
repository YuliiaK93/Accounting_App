package djrAccounting.service.implementation;

import djrAccounting.dto.CategoryDto;
import djrAccounting.entity.Category;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CategoryRepository;
import djrAccounting.service.CategoryService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return mapper.convert(categoryRepository.findById(id).orElseThrow(),CategoryDto.class);
    }

    @Override
    public List<CategoryDto> listAllCategories() {
        return null;
    }


}
