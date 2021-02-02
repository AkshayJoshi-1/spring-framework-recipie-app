package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.command.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);

    Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId);
}
