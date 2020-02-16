package com.javaer.myblog.web;

import com.javaer.myblog.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
public class indexController {
    @Resource
    private CategoryService categoryService;
    @RequestMapping("/blog")
    public String blog(HttpServletRequest request){
        request.setAttribute("allCategories", categoryService.getAll());
        return "blog/index";
    }

}
