package djrAccounting.service.implementation;

import djrAccounting.dto.CategoryDto;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CategoryRepository;
import djrAccounting.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final MapperUtil mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryDto findById(Long id) {
        return mapper.convert(categoryRepository.findById(id).orElseThrow(),CategoryDto.class);
    }
}