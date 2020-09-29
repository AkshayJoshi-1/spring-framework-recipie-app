package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/recipe")
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("{id}/show")
    public String getRecipeById(@PathVariable String id, Model model) {

        log.info("Retrieving recipe with id: " + id);
        model.addAttribute("recipe",
                            recipeService.findById(Long.valueOf(id)));

        return "recipe/show";
    }

    @RequestMapping("new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @PostMapping
    @RequestMapping("save")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/show/" + savedCommand.getId();
    }

}
