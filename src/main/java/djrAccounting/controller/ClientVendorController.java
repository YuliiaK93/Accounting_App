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
    public String listAllClientVendors(Model model){
        model.addAttribute("clientVendors", clientVendorService.listAllClientVendors());
        return "clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String createClientVendor(Model model) {

        model.addAttribute("newClientVendor", new ClientVendorDto());

        return "clientVendor/clientVendor-create";
    }
     //Add @Valid to specify that this object has validation requirements
    //Add Binding result to connect existing validation to object, MUST be right after object
    @PostMapping("/create")
    public String createClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto, BindingResult bindingResult) {
    if(bindingResult.hasErrors()){
        return "clientVendor/clientVendor-create";
    }
       clientVendorService.save(clientVendorDto);
       return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String editClientVendor(@PathVariable("id") Long id, Model model){
        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
        //need to pass ClientVendor Type
        model.addAttribute("clientVendorTypes", ClientVendorType.values());
        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String editClientVendor(@Valid @ModelAttribute("clientVendor") ClientVendorDto clientVendorDto,BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "clientVendor/clientVendor-update";
        }// problem with validation
     clientVendorService.update(clientVendorDto);
        return "redirect:/clientVendors/list";
    }


    @PostMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id){
        clientVendorService.deleteById(id);
        return "redirect:/clientVendors/list";
    }


}
