package djrAccounting.service;

import djrAccounting.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findById(Long id);

    List<CompanyDto> listAllCompanies();

    void save(CompanyDto companyDto);

    void activateCompanyStatus(Long id);

    void deactivateCompanyStatus(Long id);
}
