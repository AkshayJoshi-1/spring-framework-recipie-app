package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.command.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteByRecipeIdAndIngredientId(String recipeId, Long ingredientId);
}
