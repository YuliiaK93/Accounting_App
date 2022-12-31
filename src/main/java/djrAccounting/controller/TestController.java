package djrAccounting.controller;

import djrAccounting.repository.TestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @RequestMapping(value = {"/test"})
    public String test(Model model) {
        model.addAttribute("dbmessage", testRepository.getById(1).getTestval());
        return "/test";
    }
}
