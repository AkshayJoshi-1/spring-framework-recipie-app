package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();

    Recipe findById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);
}
