package djrAccounting.service;

import djrAccounting.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findById(Long id);

    List<CompanyDto> listAllCompanies();

}
