package com.spring.framework.recipeapp.repository.reactive;

import com.spring.framework.recipeapp.domain.Recipe;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
class RecipeReactiveRepositoryTest {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Before
    public void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void testSave() {
        Recipe recipe = new Recipe();
        recipe.setDescription("TestRecipe");

        recipeReactiveRepository.save(recipe).block();

        Long count = recipeReactiveRepository.count().block();

        assertEquals(1L, count);
    }

}