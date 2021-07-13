package raze.spring.inventory.controller.handler;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
public class CustomErrorController {
    @RequestMapping("/custom-error")
    public ModelAndView handleError( @RequestParam("message") String message) {

        return  new ModelAndView("error", Map.of("message", message));
    }


}