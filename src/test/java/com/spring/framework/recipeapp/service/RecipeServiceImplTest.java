package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.converter.RecipeCommandToRecipe;
import com.spring.framework.recipeapp.converter.RecipeToRecipeCommand;
import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.repository.RecipeRepository;
import com.spring.framework.recipeapp.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    private RecipeServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    void getAllRecipes() {

        Recipe recipe = new Recipe();
        Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipeData);

        Set<Recipe> recipes = service.getAllRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void findByIdFound() {
        Recipe recipe = new Recipe();
        recipe.setId(10L);

        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        Recipe retrievedRecipe = recipeRepository.findById(10L).orElse(null);

        assertNotNull(retrievedRecipe);
        verify(recipeRepository, times(1)).findById(10L);
    }

    @Test
    void findByIdNotFound() {
        Recipe recipe = new Recipe();
        recipe.setId(10L);

        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        Recipe retrievedRecipe = recipeRepository.findById(1L).orElse(null);

        assertNull(retrievedRecipe);
        verify(recipeRepository, times(1)).findById(1L);
    }

}