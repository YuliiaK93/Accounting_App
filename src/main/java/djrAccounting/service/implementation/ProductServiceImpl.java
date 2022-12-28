package djrAccounting.service.implementation;

import djrAccounting.dto.ProductDto;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ProductRepository;
import djrAccounting.service.ProductService;
import org.springframework.stereotype.Service;

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
}
