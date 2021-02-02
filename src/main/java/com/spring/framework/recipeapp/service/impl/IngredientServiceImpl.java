package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.command.IngredientCommand;
import com.spring.framework.recipeapp.converter.IngredientCommandToIngredient;
import com.spring.framework.recipeapp.converter.IngredientToIngredientCommand;
import com.spring.framework.recipeapp.domain.Ingredient;
import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.repository.RecipeRepository;
import com.spring.framework.recipeapp.repository.UnitOfMeasureRepository;
import com.spring.framework.recipeapp.service.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;

    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(@Autowired RecipeRepository recipeRepository,
                                 @Autowired IngredientToIngredientCommand ingredientToIngredientCommand,
                                 @Autowired IngredientCommandToIngredient ingredientCommandToIngredient,
                                 @Autowired UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {


        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()){
            log.error("recipe id not found. Id: " + recipeId);
        }
        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert).findFirst();
        if(ingredientCommandOptional.isEmpty()){
            log.error("Ingredient id not found: " + ingredientId);
        }

        //enhance command object with recipe id
        IngredientCommand ingredientCommand = ingredientCommandOptional.get();
        ingredientCommand.setRecipeId(recipe.getId());

        return Mono.just(ingredientCommandOptional.get());
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(recipeOptional.isEmpty()) {
            log.error("No recipe with id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                                                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                                                    .findFirst();

        if(ingredientOptional.isEmpty()) {
            return Mono.just(saveNewIngredient(command, recipe));

        } else {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                    .findById(command.getUnitOfMeasure().getId())
                    .orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Utils.fillIngredientRecipes(savedRecipe);

        return Mono.just(Objects.requireNonNull(ingredientToIngredientCommand.convert(Objects.requireNonNull(savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst().orElse(null)))));
    }

    @Override
    public Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isEmpty()) {
            return null;
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                                                            .filter(ingredient -> false)
                                                            .findFirst();

        return Mono.empty();
    }

    private IngredientCommand saveNewIngredient(IngredientCommand command, Recipe recipe) {

        Set<String> currentIngredients = recipe.getIngredients().stream()
                                                .map(Ingredient::getId)
                                                .collect(Collectors.toSet());

        Ingredient ingredient = ingredientCommandToIngredient.convert(command);
        recipe.addIngredient(ingredient);
        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> ingredientOptional = savedRecipe.getIngredients().stream()
                                                                .filter(ingredient1 -> !currentIngredients.contains(ingredient1.getId()))
                                                                .findFirst();

        return ingredientToIngredientCommand.convert(ingredientOptional.orElse(null));
    }


}
