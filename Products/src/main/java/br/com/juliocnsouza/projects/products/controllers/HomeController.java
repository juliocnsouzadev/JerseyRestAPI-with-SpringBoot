package br.com.juliocnsouza.projects.products.controllers;

import br.com.juliocnsouza.projects.products.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author julio
 */
@Controller
public class HomeController {

    @Autowired
    private DataInitializer dataInitializer;

    @RequestMapping( "/" )
    @ResponseBody
    public String home() {
        final long[] counts = dataInitializer.init();
        return "Products REST API. Total of " + counts[0] + " products and " + counts[1] + " images.";
    }

}
