package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.exception.NotFoundException;
import com.spring.framework.recipeapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    @RequestMapping("{id}/show")
    public String getRecipeById(@PathVariable String id, Model model) {

        log.info("Retrieving recipe with id: " + id);
        model.addAttribute("recipe",
                            recipeService.findById(id).block());

        return "recipe/show";
    }

    @GetMapping
    @RequestMapping("new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @GetMapping
    @RequestMapping("{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id).block());
        return "recipe/recipeform";
    }

    @PostMapping
    @RequestMapping("save")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command).block();

        return "redirect:/recipe/" + savedCommand.getId() + "/show" ;
    }

    @GetMapping
    @RequestMapping("{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(id).block();

        return "redirect:/";
    }

    /*@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception exception){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("404ErrorPage");
        mv.addObject("exception", exception);

        return mv;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNUmberFormatException(Exception exception){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("badRequest");
        mv.addObject("exception", exception);

        return mv;
    }*/
}
