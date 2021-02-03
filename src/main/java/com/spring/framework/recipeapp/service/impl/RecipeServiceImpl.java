package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.converter.RecipeCommandToRecipe;
import com.spring.framework.recipeapp.converter.RecipeToRecipeCommand;
import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.repository.reactive.RecipeReactiveRepository;
import com.spring.framework.recipeapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(@Autowired RecipeReactiveRepository recipeRepository,
                             @Autowired RecipeCommandToRecipe recipeCommandToRecipe,
                             @Autowired RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getAllRecipes() {

        log.info("Getting all recipe");

        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {

        return recipeRepository.findById(id);
    }



    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {

        if(command == null) {
            return Mono.empty();
        }

        return recipeRepository.save(Objects.requireNonNull(recipeCommandToRecipe.convert(command)))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {

        return recipeRepository.findById(id)
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeRepository.deleteById(id);
    }
}
