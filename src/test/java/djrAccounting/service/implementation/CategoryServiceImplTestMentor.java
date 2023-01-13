package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.dto.CategoryDto;
import djrAccounting.entity.Category;
import djrAccounting.exception.CategoryNotFoundException;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @DisplayName("When category is searched with existing category id, " +
            "it should return a valid Category object")
    void findById_Test(long id){
        CategoryDto categoryDto = TestConstants.getTestCategoryDto();
        categoryDto.setId(id);
        Category category = mapperUtil.convert(categoryDto, Category.class);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryDto returnedCategoryDto = categoryService.findById(id);

        verify(categoryRepository).findById(id);
        assertEquals(returnedCategoryDto.getId(), categoryDto.getId());
    }

    @Test
    @DisplayName("When category is searched with non-existing category id, " +
            "it should throw CategoryNotFoundException")
    void findById_Throws_Test(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(TestConstants.SAMPLE_ID1));
    }

    @Test
    void deleteCategoryById_Test(){
        Category category = Category.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(category.getId());
        assertTrue(category.getIsDeleted());
    }

    @Test
    @DisplayName("When category is searched with non-existing category id, it should throw CategoryNotFoundException")
    void deleteCategoryById_Throws_Test(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(TestConstants.SAMPLE_ID1));
    }
}