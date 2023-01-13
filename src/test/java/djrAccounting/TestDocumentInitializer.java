package djrAccounting;

import djrAccounting.dto.*;
import djrAccounting.entity.Address;
import djrAccounting.entity.Company;
import djrAccounting.entity.User;
import djrAccounting.enums.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static djrAccounting.TestConstants.*;

public class TestDocumentInitializer {

    public static User getTestUser(RoleEnum roleEnum) {
        return User.builder()
                .id(SAMPLE_ID1)
                .firstname(SAMPLE_FIRST_NAME_JOHN)
                .lastname(SAMPLE_LAST_NAME_JOHN)
                .phone(SAMPLE_PHONE_NUMBER1)
                .password(PASSWORD_ABC1)
                .role(djrAccounting.entity.Role.builder().description(RoleEnum.ADMIN.getValue()).build())
                .company(getTestCompany(CompanyStatus.ACTIVE))
                .build();
    }

    public static Company getTestCompany(CompanyStatus status) {
        return Company.builder()
                .title(SAMPLE_COMPANY1)
                .website(SAMPLE_WEB_SITE1)
                .id(SAMPLE_ID1)
                .phone(SAMPLE_PHONE_NUMBER1)
                .companyStatus(status)
                .address(new Address())
                .build();
    }

    public static UserDto getTestUserDto(RoleEnum roleEnum) {
        return UserDto.builder()
                .id(SAMPLE_ID1)
                .firstname(SAMPLE_FIRST_NAME_JOHN)
                .lastname(SAMPLE_LAST_NAME_JOHN)
                .phone(SAMPLE_PHONE_NUMBER1)
                .password(PASSWORD_ABC1)
                .confirmPassword(PASSWORD_ABC1)
                .role(new RoleDto(SAMPLE_ID1, roleEnum.getValue()))
                .isOnlyAdmin(false)
                .company(getTestCompanyDto(CompanyStatus.ACTIVE))
                .build();
    }

    public static CompanyDto getTestCompanyDto(CompanyStatus status) {
        return CompanyDto.builder()
                .title(SAMPLE_COMPANY1)
                .website(SAMPLE_WEB_SITE1)
                .id(SAMPLE_ID1)
                .phone(SAMPLE_PHONE_NUMBER1)
                .companyStatus(status)
                .address(new AddressDto())
                .build();
    }

    public static CategoryDto getTestCategoryDto() {
        return CategoryDto.builder()
                .company(getTestCompanyDto(CompanyStatus.ACTIVE))
                .description(SAMPLE_CATEGORY1)
                .build();
    }

    public static ClientVendorDto getTestClientVendorDto(ClientVendorType type) {
        return ClientVendorDto.builder()
                .clientVendorType(type)
                .clientVendorName(SAMPLE_CLIENT1)
                .address(new AddressDto())
                .website(SAMPLE_WEB_SITE1)
                .phone(SAMPLE_PHONE_NUMBER1)
                .build();
    }

    public static ProductDto getTestProductDto() {
        return ProductDto.builder()
                .category(getTestCategoryDto())
                .productUnit(ProductUnit.PCS)
                .name(SAMPLE_PRODUCT1)
                .quantityInStock(SAMPLE_QUANTITY_IN_STOCK_10)
                .lowLimitAlert(SAMPLE_LOW_LIMIT_ALERT5)
                .build();
    }

    public static InvoiceProductDto getTestInvoiceProductDto() {
        return InvoiceProductDto.builder()
                .product(getTestProductDto())
                .price(BigDecimal.TEN)
                .tax(SAMPLE_TAX_RATE10)
                .quantity(SAMPLE_QUANTITY10)
                .invoice(new InvoiceDto())
                .build();
    }

    public static InvoiceDto getTestInvoiceDto(InvoiceStatus status, InvoiceType type) {
        return InvoiceDto.builder()
                .invoiceNo(type == InvoiceType.PURCHASE ? SAMPLE_PURCHASE_INVOICE_NO1 : SAMPLE_SALES_INVOICE_NO1)
                .clientVendor(getTestClientVendorDto(ClientVendorType.CLIENT))
                .invoiceStatus(status)
                .invoiceType(type)
                .date(SAMPLE_DATE_2022_1_1)
                .company(getTestCompanyDto(CompanyStatus.ACTIVE))
                .invoiceProducts(new ArrayList<>(Arrays.asList(getTestInvoiceProductDto())))
                .price(SAMPLE_PRICE1000)
                .tax(BigDecimal.TEN)
                .total(BigDecimal.TEN.multiply(SAMPLE_PRICE1000))
                .build();
    }
}
