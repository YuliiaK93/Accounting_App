package djrAccounting.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.Company;
import djrAccounting.enums.CompanyStatus;
import djrAccounting.exception.CompanyNotFoundException;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CompanyRepository;
import djrAccounting.service.CompanyService;
import djrAccounting.service.SecurityService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapper;
    private final SecurityService securityService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.companyRepository = companyRepository;
        this.mapper = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public CompanyDto findById(Long id) {
        return mapper.convert(
                companyRepository.findById(id)
                        .orElseThrow(() -> new CompanyNotFoundException("Company not found this id: " + id)),
                new CompanyDto());
    }

    @Override
    public List<CompanyDto> listAllCompanies() {
        return companyRepository.findAll(Sort.by("title")).stream()
                .filter(company -> company.getId() != 1)
                .map(company -> mapper.convert(company, new CompanyDto()))
                .sorted(Comparator.comparing(CompanyDto::getCompanyStatus))
                .collect(Collectors.toList());
    }

    @Override
    public void activateCompanyStatus(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new CompanyNotFoundException("Company not found this id: " + id));
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public void deactivateCompanyStatus(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new CompanyNotFoundException("Company not found this id: " + id));
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }

    @Override
    public CompanyDto update(CompanyDto companyDto) {
        Company dbCompany = companyRepository.findById(companyDto.getId()).orElseThrow(
                () -> new CompanyNotFoundException("Company not found this id: " + companyDto.getId()));
        Company convertedCompany = mapper.convert(companyDto, Company.class);
        convertedCompany.setCompanyStatus(dbCompany.getCompanyStatus());
        companyRepository.save(convertedCompany);
        return mapper.convert(convertedCompany, CompanyDto.class);
    }

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        Company company = mapper.convert(companyDto, Company.class);
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
        return mapper.convert(company, CompanyDto.class);
    }

    @Override
    public boolean isTitleExist(String title) {
        return companyRepository.existsByTitle(title);
    }

    @Override
    public boolean isTitleExistExceptCurrentCompanyTitle(CompanyDto companyDto) {
        Company company = mapper.convert(findById(companyDto.getId()), new Company());
        if (company.getTitle().equals(companyDto.getTitle())) {
            return false;
        }
        return companyRepository.existsByTitle(companyDto.getTitle());
    }

    @Override
    public List<CompanyDto> listCompaniesByLoggedInUser() {
        UserDto loggedInUser = securityService.getLoggedInUser();
        if ("Admin".equals(loggedInUser.getRole().getDescription())) {
            return listAllCompanies().stream()
                    .filter(company -> company.getId().equals(loggedInUser.getCompany().getId()))
                    .collect(Collectors.toList());
        }
        return listAllCompanies();
    }
}