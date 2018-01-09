package hello.appmaster;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContainerAllocationController {

    @RequestMapping(method = RequestMethod.GET, value = "/allocateContainers")
    public String containersAllocation(Model model) {
        model.addAttribute("yarnAppParameters", new YarnAppParameters());
        return "allocateContainers";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/yarnAppParameters")
    public String yarnAppParametersSubmit(@ModelAttribute YarnAppParameters yarnAppParameters) {
        return "result";
    }

}