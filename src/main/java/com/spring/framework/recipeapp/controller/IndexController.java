package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.repository.CategoryRepository;
import com.spring.framework.recipeapp.repository.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setUnitOfMeasureRepository(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(){

        System.out.println(categoryRepository.findByDescription("American").get().getId());

        return "index";
    }
}
