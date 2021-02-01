package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.domain.Ingredient;
import com.spring.framework.recipeapp.domain.Recipe;

public class Utils {

    public static void fillIngredientRecipes(Recipe recipe) {
        for(Ingredient ingredient : recipe.getIngredients()) {
            ingredient.setRecipe(recipe);
        }
    }
}
