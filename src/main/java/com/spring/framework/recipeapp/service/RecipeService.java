package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();
}
