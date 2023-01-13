package djrAccounting.unit.service.implementation;

import djrAccounting.TestDocumentInitializer;
import djrAccounting.dto.CategoryDto;
import djrAccounting.entity.Category;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CategoryRepository;
import djrAccounting.service.SecurityService;
import djrAccounting.service.implementation.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTestMentor {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void findById_Test(long id){
        CategoryDto categoryDto = TestDocumentInitializer.getTestCategoryDto();
        categoryDto.setId(id);
        Category category = mapperUtil.convert(categoryDto, Category.class);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryDto returnedCategoryDto = categoryService.findById(id);

        verify(categoryRepository).findById(id);
        assertEquals(returnedCategoryDto.getId(), categoryDto.getId());
    }


    @Test
    void deleteCategoryById_Test(){
        Category category = new Category(){{
            setId(1L);
            setIsDeleted(false);
        }};

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        categoryService.deleteCategoryById(category.getId());
        assertTrue(category.getIsDeleted());
    }
}
