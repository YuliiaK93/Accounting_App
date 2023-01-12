package djrAccounting.service.implementation;

import djrAccounting.dto.CategoryDto;
import djrAccounting.entity.Category;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CategoryRepository;
import djrAccounting.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    MapperUtil mapperUtil;

    @Mock
    SecurityService securityService;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void findById_Test(long id){

        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(mapperUtil.convert(any(Category.class), eq(CategoryDto.class))).thenReturn(new CategoryDto());

        categoryService.findById(id);

        verify(categoryRepository).findById(id);
        verify(mapperUtil).convert(any(Category.class), eq(CategoryDto.class));
    }


    @Test
    void deleteCategoryById_Test(){
        Category category = new Category(){{
            setId(1L);
            setIsDeleted(false);
        }};

        Category category1 = new Category(){{
            setId(1L);
            setIsDeleted(false);
        }};

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(eq(category))).thenReturn(category1);
        categoryService.deleteCategoryById(category.getId());
        assertTrue(category.getIsDeleted());
    }
}
