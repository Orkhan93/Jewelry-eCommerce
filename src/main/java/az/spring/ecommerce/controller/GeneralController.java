package az.spring.ecommerce.controller;

import az.spring.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class GeneralController {

    private final ProductService productService;

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public ModelAndView homePage() {
//        ModelAndView modelAndView = new ModelAndView("index");
//        modelAndView.addObject("products",)
//        return modelAndView;
//    }

}