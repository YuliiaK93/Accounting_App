package djrAccounting.controller;

import djrAccounting.bootstrap.StaticConstants;
import djrAccounting.dto.CompanyDto;
import djrAccounting.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String getCompanies(Model model) {

        model.addAttribute("companies", companyService.listAllCompanies());

        return "company/company-list";
    }

    @GetMapping("/create")
    public String createCompany(Model model) {

        model.addAttribute("newCompany", new CompanyDto());

        return "company/company-create";
    }

    @PostMapping("/create")
    public String insertCompany(@ModelAttribute("newCompany") CompanyDto company) {

        companyService.save(company);

        return "redirect:/companies/list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {

        model.addAttribute("company", companyService.findById(id));
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);

        return "company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@ModelAttribute("company") CompanyDto companyDto) {

        companyService.update(companyDto);

        return "redirect:/companies/list";
    }


    @GetMapping("/activate/{id}")
    public String activateCompany(@PathVariable("id") Long id) {
        companyService.activateCompanyStatus(id);
        return "redirect:/companies/list";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id") Long id) {
        companyService.deactivateCompanyStatus(id);
        return "redirect:/companies/list";
    }
}
