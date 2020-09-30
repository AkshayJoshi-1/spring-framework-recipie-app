package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.service.IngredientService;
import com.spring.framework.recipeapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/recipe")
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    public IngredientController(@Autowired RecipeService recipeService,
                                @Autowired IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model) {

        log.debug("Getting ingredients for id: " + id);
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/ingredients/list";
    }

    @GetMapping
    @RequestMapping("{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId,
                                 @PathVariable String ingredientId,
                                 Model model) {

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(
                                                                Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        return "recipe/ingredients/show";

    }
}
