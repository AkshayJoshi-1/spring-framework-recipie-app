package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.converter.RecipeCommandToRecipe;
import com.spring.framework.recipeapp.converter.RecipeToRecipeCommand;
import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.exception.NotFoundException;
import com.spring.framework.recipeapp.repository.RecipeRepository;
import com.spring.framework.recipeapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(@Autowired RecipeRepository recipeRepository,
                             @Autowired RecipeCommandToRecipe recipeCommandToRecipe,
                             @Autowired RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getAllRecipes() {

        log.info("Getting all recipe");
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    public Recipe findById(String id) {
        Optional<Recipe> recipeOption = recipeRepository.findById(id);

        if(recipeOption.isEmpty()) {
            throw new NotFoundException("Unable to find recipe with ID: " + id);
        }

        return recipeOption.get();
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {

        if(command == null) {
            return null;
        }

        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(String id) {
        return recipeToRecipeCommand.convert(recipeRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteById(String id) {
        recipeRepository.deleteById(id);
    }
}
