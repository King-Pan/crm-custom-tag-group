package com.asiainfo.tag.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/21
 * Time: 下午4:20
 * Description: No Description
 */
@RestController
public class HomeController {


    @RequestMapping("/")
    public ModelAndView page() {
        return new ModelAndView("home");
    }
}
