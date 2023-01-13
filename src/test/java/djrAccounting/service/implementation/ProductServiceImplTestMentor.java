package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.entity.Product;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ProductRepository;
import djrAccounting.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTestMentor {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private MapperUtil mapper;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    void deleteProductById() {
        //given
        Product product = Product.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).build();

        //when
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(productRepository.save(eq(product))).thenReturn(any(Product.class));

        //then
        productServiceImpl.deleteProductById(product.getId());
        assertTrue(product.getIsDeleted());
    }
}