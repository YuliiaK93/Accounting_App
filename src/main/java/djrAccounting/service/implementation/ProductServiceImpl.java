package djrAccounting.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.dto.ProductDto;
import djrAccounting.entity.Company;
import djrAccounting.entity.Product;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ProductRepository;
import djrAccounting.service.ProductService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SecurityService securityService;
    private final MapperUtil mapper;

    public ProductServiceImpl(ProductRepository productRepository, SecurityService securityService, MapperUtil mapper) {
        this.productRepository = productRepository;
        this.securityService = securityService;
        this.mapper = mapper;
    }

    @Override
    public ProductDto findById(Long id) {
        return mapper.convert(productRepository.findById(id).orElseThrow(), ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        return productRepository.findAll().stream().filter(product -> product.getCategory().getCompany().getId() == companyId).map(product -> mapper.convert(product, new ProductDto())).collect(Collectors.toList());
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();

        // if (productDto.getQuantityInStock()>0 || )
        // TODO: 12/28/2022
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public boolean productExistByCategory(Long categoryId){
        return productRepository.existsByCategory_Id(categoryId);
    }

    @Override
    public List<ProductDto> listProductsBySelectedUserCompany() {
        CompanyDto companyDto = securityService.getLoggedInUser().getCompany();
        Company company = mapper.convert(companyDto, Company.class);

        return productRepository.findAllByCategoryCompany(company)
                .stream()
                .map(product -> mapper.convert(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStockEnough(InvoiceProductDto invoiceProductDto) {
        int remainingStock = productRepository.findByName(invoiceProductDto.getProduct().getName()).getQuantityInStock();
        return remainingStock > invoiceProductDto.getQuantity();
    }
    @Override
    public void update(ProductDto productDto) {
        productRepository.save(mapper.convert(productDto, Product.class));
    }
    @Override
    public void save(ProductDto productDto) {
        productRepository.save(mapper.convert(productDto, Product.class));
    }
}
