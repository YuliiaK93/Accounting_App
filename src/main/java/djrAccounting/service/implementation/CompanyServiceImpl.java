package djrAccounting.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.CompanyRepository;
import djrAccounting.service.CompanyService;
import org.springframework.stereotype.Service;

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
        return companyRepository.findAll().stream()
                .map(company -> mapper.convert(company, new CompanyDto()))
                .collect(Collectors.toList());
    }

}
