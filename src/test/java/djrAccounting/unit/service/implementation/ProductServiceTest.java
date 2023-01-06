package djrAccounting.unit.service.implementation;

import djrAccounting.entity.Category;
import djrAccounting.entity.Company;
import djrAccounting.entity.Product;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ProductRepository;
import djrAccounting.service.ProductService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.implementation.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {


    private ProductServiceImpl productServiceImpl;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private  SecurityService securityService;

    @Mock
    private  MapperUtil mapper;


    @BeforeEach
    void setUp() {
        productServiceImpl = new ProductServiceImpl(productRepository, securityService , mapper);
    }

    @Test
    void findById() {
    }

    @Test
    void getAllProducts() {

    }


    @Test
    void deleteProductById() {
        Product product = new Product(){{
            setId(1L);
            setIsDeleted(false);
        }};
        Product product2 = new Product(){{
            setId(1L);
            setIsDeleted(true);
        }};
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(productRepository.save(eq(product))).thenReturn(product2);
        productServiceImpl.deleteProductById(product.getId());
        assertTrue(product.getIsDeleted());
    }




    @Test
    void productExistByCategory() {
    }

    @Test
    void listProductsBySelectedUserCompany() {
    }

    @Test
    void isStockEnough() {
    }

    @Test
    void update() {
    }

    @Test
    void save() {
    }
}