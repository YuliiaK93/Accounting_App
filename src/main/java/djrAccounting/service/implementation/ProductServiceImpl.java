package djrAccounting.service.implementation;

import djrAccounting.dto.ProductDto;
import djrAccounting.entity.Product;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ProductRepository;
import djrAccounting.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final MapperUtil mapper;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public ProductDto findById(Long id) {
        return mapper.convert(productRepository.findById(id).orElseThrow(),ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(product -> mapper.convert(product, new ProductDto())).collect(Collectors.toList());
    }

    @Override
    public void deleteProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow();

       // if (productDto.getQuantityInStock()>0 || )
        // TODO: 12/28/2022
        product.setIsDeleted(true);
        productRepository.save(product);
    }
}
