package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.repository.CategoryRepository;
import com.spring.framework.recipeapp.repository.UnitOfMeasureRepository;
import com.spring.framework.recipeapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private RecipeService recipeService;

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model){

        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "index";
    }
}
