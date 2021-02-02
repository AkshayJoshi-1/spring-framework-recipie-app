package com.spring.framework.recipeapp.repository.reactive;

import com.spring.framework.recipeapp.domain.Category;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
class CategoryReactiveRepositoryTest {

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Before
    public void setUp() {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void testSave() {
        Category category = new Category();
        category.setDescription("Category1");

        categoryReactiveRepository.save(category).block();

        Long count = categoryReactiveRepository.count().block();

        assertEquals(1L, count);
    }

    @Test
    public void testFindByDescription() {

        Category retrievedCategory = categoryReactiveRepository.findByDescription("Category1").block();

        assertNotNull(retrievedCategory);
    }
}