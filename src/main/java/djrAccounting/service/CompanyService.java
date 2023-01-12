package djrAccounting.service;

import djrAccounting.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    CompanyDto findById(Long id);

    List<CompanyDto> listAllCompanies();

    CompanyDto save(CompanyDto companyDto);

    void activateCompanyStatus(Long id);

    void deactivateCompanyStatus(Long id);

    CompanyDto update(CompanyDto companyDto);

    boolean isTitleExist(String title);

    boolean isTitleExistExceptCurrentCompanyTitle(CompanyDto companyDto);

    List<CompanyDto> listCompaniesByLoggedInUser();
}
