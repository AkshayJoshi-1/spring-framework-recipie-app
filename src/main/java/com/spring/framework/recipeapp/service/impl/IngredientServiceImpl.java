package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.command.IngredientCommand;
import com.spring.framework.recipeapp.converter.IngredientToIngredientCommand;
import com.spring.framework.recipeapp.domain.Ingredient;
import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.repository.RecipeRepository;
import com.spring.framework.recipeapp.service.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;

    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(@Autowired RecipeRepository recipeRepository,
                                 @Autowired IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isEmpty()) {
            log.error("Recipe with id " + recipeId + " does not exist.");
            return null;
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                                                                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                                                                    .findFirst();

        if(ingredientOptional.isEmpty()) {
            log.error("Ingredient with id " + ingredientId + " does not exist for recipe with id " + recipeId);
            return null;
        }

        return ingredientToIngredientCommand.convert(ingredientOptional.get());
    }
}
