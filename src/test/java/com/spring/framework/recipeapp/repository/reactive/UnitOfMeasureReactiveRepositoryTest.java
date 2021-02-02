package com.spring.framework.recipeapp.repository.reactive;

import com.spring.framework.recipeapp.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
class UnitOfMeasureReactiveRepositoryTest {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Before
    public void setUp() {
        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    public void testSave() {

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription("Each");

        unitOfMeasureReactiveRepository.save(uom).block();
        Long count = unitOfMeasureReactiveRepository.count().block();

        assertEquals(1L, count);
    }

    @Test
    public void testFindByDescription() {
        UnitOfMeasure retrievedUom = unitOfMeasureReactiveRepository.findByDescription("Each").block();
        assertNotNull(retrievedUom);
    }

}