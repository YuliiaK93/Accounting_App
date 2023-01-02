package djrAccounting.controller;

import djrAccounting.bootstrap.StaticConstants;
import djrAccounting.dto.ClientVendorDto;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.service.ClientVendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;

    public ClientVendorController(ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String listAllClientVendors(Model model) {
        model.addAttribute("clientVendors", clientVendorService.listAllClientVendors());
        return "clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String createClientVendor(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("clientVendorTypes", ClientVendorType.values());
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
        return "clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String createClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto, BindingResult bindingResult, Model model) {
        boolean duplicatedName = clientVendorService.nameExists(clientVendorDto.getClientVendorName());
        if (bindingResult.hasErrors() || duplicatedName) {
            if (duplicatedName) {
                bindingResult.rejectValue("clientVendorName", " ", "A Client/Vendor with this name already exists. Please, try again.");
            }
            model.addAttribute("clientVendorTypes", ClientVendorType.values());
            model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
            return "clientVendor/clientVendor-create";
        }

        clientVendorService.save(clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String editClientVendor(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
        model.addAttribute("clientVendorTypes", ClientVendorType.values());
        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String editClientVendor(@Valid @ModelAttribute("clientVendor") ClientVendorDto clientVendorDto, BindingResult bindingResult,@PathVariable("id") Long id, Model model){
            boolean duplicatedName = clientVendorService.nameExists(clientVendorDto.getClientVendorName());
            if(bindingResult.hasErrors() || duplicatedName){
                if(duplicatedName){
                    bindingResult.rejectValue("clientVendorName"," ", "A Client/Vendor with this name already exists. Please, try again.");
                }
                model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
                model.addAttribute("clientVendorTypes", ClientVendorType.values());
                return "clientVendor/clientVendor-update";
            }
            clientVendorService.update(clientVendorDto);
            return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clientVendors", clientVendorService.listAllClientVendors());
        try {
            clientVendorService.deleteById(id);
        } catch (IllegalAccessException e) {
            model.addAttribute("error", e.getMessage());
            return "clientVendor/clientVendor-list";
        }
        return "redirect:/clientVendors/list";
    }
}
