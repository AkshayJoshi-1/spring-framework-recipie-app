package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface RecipeService {
    Flux<Recipe> getAllRecipes();

    Mono<Recipe> findById(String id);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);

    Mono<RecipeCommand> findCommandById(String id);

    Mono<Void> deleteById(String id);
}
