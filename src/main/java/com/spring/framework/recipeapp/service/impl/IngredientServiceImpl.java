package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.command.IngredientCommand;
import com.spring.framework.recipeapp.converter.IngredientCommandToIngredient;
import com.spring.framework.recipeapp.converter.IngredientToIngredientCommand;
import com.spring.framework.recipeapp.domain.Ingredient;
import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.repository.RecipeRepository;
import com.spring.framework.recipeapp.repository.reactive.RecipeReactiveRepository;
import com.spring.framework.recipeapp.repository.reactive.UnitOfMeasureReactiveRepository;
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

    private final RecipeReactiveRepository recipeReactiveRepository;

    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    public IngredientServiceImpl(@Autowired RecipeRepository recipeRepository,
                                 RecipeReactiveRepository recipeReactiveRepository, @Autowired IngredientToIngredientCommand ingredientToIngredientCommand,
                                 @Autowired IngredientCommandToIngredient ingredientCommandToIngredient,
                                 @Autowired UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .single()
                .map(ingredientToIngredientCommand::convert);
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(recipeOptional.isEmpty()){

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                        .findById(command.getUnitOfMeasure().getId()).block());

                //        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
                if (ingredientFound.getUnitOfMeasure() == null){
                    throw new RuntimeException("UOM NOT FOUND");
                }
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if(!savedIngredientOptional.isPresent()){
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            //enhance with id value
            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());

            return Mono.just(ingredientCommandSaved);
        }
    }

    @Override
    public Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isEmpty()) {
            return null;
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                                                            .filter(ingredient -> ingredient.getId().equals(ingredientId))
                                                            .findFirst();

        if(ingredientOptional.isEmpty()) {
            return Mono.empty();
        }

        recipe.getIngredients().remove(ingredientOptional.get());

        recipeRepository.save(recipe);

        return Mono.empty();
    }
}
