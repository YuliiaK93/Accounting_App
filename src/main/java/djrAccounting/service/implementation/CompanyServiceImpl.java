package djrAccounting.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.entity.Company;
import djrAccounting.enums.CompanyStatus;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CompanyRepository;
import djrAccounting.service.CompanyService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil) {
        this.companyRepository = companyRepository;
        this.mapper = mapperUtil;
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
        companyRepository.save(company);
    }

    @Override
    public void deactivateCompanyStatus(Long id) {
        Company company = mapper.convert(findById(id), Company.class);
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }

    @Override
    public void save(CompanyDto companyDto) {

        Company company = mapper.convert(companyDto, Company.class);
        company.setCompanyStatus(CompanyStatus.ACTIVE);

        companyRepository.save(company);
    }

}
