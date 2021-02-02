package com.spring.framework.recipeapp.repository.reactive;

import com.spring.framework.recipeapp.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
