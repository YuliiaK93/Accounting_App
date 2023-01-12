package djrAccounting;

import djrAccounting.dto.*;
import djrAccounting.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class TestDocumentInitializer {

    public static UserDto getTestUserDto(Role role){
        return UserDto.builder()
                .id(1L)
                .firstname("John")
                .lastname("Mike")
                .phone("+1 (111) 111-1111")
                .password("Abc1")
                .confirmPassword("Abc1")
                .role(new RoleDto(1L, role.getValue()))
                .isOnlyAdmin(false)
                .company(getTestCompanyDto(CompanyStatus.ACTIVE))
                .build();
    }

    public static CompanyDto getTestCompanyDto(CompanyStatus status){
        return CompanyDto.builder()
                .title("Test_Company")
                .website("www.test.com")
                .id(1L)
                .phone("+1 (111) 111-1111")
                .companyStatus(status)
                .address(new AddressDto())
                .build();
    }

    public static CategoryDto getTestCategoryDto(){
        return CategoryDto.builder()
                .company(getTestCompanyDto(CompanyStatus.ACTIVE))
                .description("Test_Category")
                .build();
    }

    public static ClientVendorDto getTestClientVendorDto(ClientVendorType type){
        return ClientVendorDto.builder()
                .clientVendorType(type)
                .clientVendorName("Test_ClientVendor")
                .address(new AddressDto())
                .website("https://www.test.com")
                .phone("+1 (111) 111-1111")
                .build();
    }

    public static ProductDto getTestProductDto(){
        return ProductDto.builder()
                .category(getTestCategoryDto())
                .productUnit(ProductUnit.PCS)
                .name("Test_Product")
                .quantityInStock(10)
                .lowLimitAlert(5)
                .build();
    }

    public static InvoiceProductDto getTestInvoiceProductDto(){
        return InvoiceProductDto.builder()
                .product(getTestProductDto())
                .price(BigDecimal.TEN)
                .tax(10)
                .quantity(10)
                .invoice(new InvoiceDto())
                .build();
    }

    public static InvoiceDto getTestInvoiceDto(InvoiceStatus status, InvoiceType type){
        return InvoiceDto.builder()
                .invoiceNo("T-001")
                .clientVendor(getTestClientVendorDto(ClientVendorType.CLIENT))
                .invoiceStatus(status)
                .invoiceType(type)
                .date(LocalDate.of(2022,01,01))
                .company(getTestCompanyDto(CompanyStatus.ACTIVE))
                .invoiceProducts(new ArrayList<>(Arrays.asList(getTestInvoiceProductDto())))
                .price(BigDecimal.valueOf(1000))
                .tax(BigDecimal.TEN)
                .total(BigDecimal.TEN.multiply(BigDecimal.valueOf(1000)))
                .build();
    }
}
