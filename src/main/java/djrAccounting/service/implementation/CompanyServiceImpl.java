package djrAccounting.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.Company;
import djrAccounting.enums.CompanyStatus;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CompanyRepository;
import djrAccounting.service.CompanyService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapper;
    private final UserService userService;

    private final SecurityService securityService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, UserService userService, SecurityService securityService) {
        this.companyRepository = companyRepository;
        this.mapper = mapperUtil;
        this.userService = userService;
        this.securityService = securityService;
    }

    @Override
    public CompanyDto findById(Long id) {
        return mapper.convert(companyRepository.findById(id).orElseThrow(), CompanyDto.class);
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
        Company company = mapper.convert(findById(id), Company.class);
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        // userService.makeUserEnableByCompany(company);
        companyRepository.save(company);
    }

    @Override
    public void deactivateCompanyStatus(Long id) {
        Company company = mapper.convert(findById(id), Company.class);
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        //userService.makeUserDisableByCompany(company);
        companyRepository.save(company);
    }

    @Override
    public CompanyDto update(CompanyDto companyDto) {
        Company dbCompany = companyRepository.findById(companyDto.getId()).orElseThrow();
        Company convertedCompany = mapper.convert(companyDto, Company.class);
        convertedCompany.setCompanyStatus(dbCompany.getCompanyStatus());
        companyRepository.save(convertedCompany);
        return companyDto;
    }

    @Override
    public void save(CompanyDto companyDto) {
        Company company = mapper.convert(companyDto, Company.class);
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public boolean isTitleExist(String title) {
        return companyRepository.existsByTitle(title);
    }

    @Override
    public List<CompanyDto> listCompaniesByLoggedInUser() {
        UserDto loggedInUser = securityService.getLoggedInUser();
        switch (loggedInUser.getRole().getDescription()) {
            case "Admin":
                return listAllCompanies().stream()
                        .filter(company -> company.getId().equals(loggedInUser.getCompany().getId()))
                        .collect(Collectors.toList());
            default:
                return listAllCompanies();
        }
    }
}