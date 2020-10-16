package main.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    private final main.functional.Controller controller;

    public TemplateController(main.functional.Controller controller) {
        this.controller = controller;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("sources", controller.getSources());
        model.addAttribute("devices", controller.getDevices());
        model.addAttribute("sourcesConfiguration", controller.getSourcesConfiguration());
        model.addAttribute("bufferConfiguration", controller.getBufferConfiguration());
        model.addAttribute("devicesConfiguration", controller.getDevicesConfiguration());
        model.addAttribute("systemConfiguration", controller.getSystemConfiguration());
        return "homePage";
    }

}
