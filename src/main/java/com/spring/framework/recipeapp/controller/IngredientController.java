package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.command.IngredientCommand;
import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.command.UnitOfMeasureCommand;
import com.spring.framework.recipeapp.service.IngredientService;
import com.spring.framework.recipeapp.service.RecipeService;
import com.spring.framework.recipeapp.service.UnitOfMeasurementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/recipe")
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final UnitOfMeasurementService unitOfMeasurementService;

    public IngredientController(@Autowired RecipeService recipeService,
                                @Autowired IngredientService ingredientService,
                                @Autowired UnitOfMeasurementService unitOfMeasurementService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasurementService = unitOfMeasurementService;
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

    @GetMapping
    @RequestMapping("{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String recipeId,
                                   @PathVariable String ingredientId,
                                   Model model) {

        model.addAttribute("uomList", unitOfMeasurementService.listAllUoms());

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(
                                                                Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        return "recipe/ingredients/ingredientform";
    }

    @GetMapping
    @RequestMapping("{recipeId}/ingredient/new")
    public String createIngredient(@PathVariable String recipeId, Model model) {

        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasurementService.listAllUoms());

        model.addAttribute("ingredient", ingredientCommand);

        return "recipe/ingredients/ingredientform";
    }

    @PostMapping
    @RequestMapping("{recipeId}/ingredient")
    public String saveUpdateIngredient(@ModelAttribute IngredientCommand command) {

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId,
                                   @PathVariable String ingredientId) {
        ingredientService.deleteByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId));

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}