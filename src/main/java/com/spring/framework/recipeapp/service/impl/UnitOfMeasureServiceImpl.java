package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.command.UnitOfMeasureCommand;
import com.spring.framework.recipeapp.converter.UnitOfMeasureToUnitOfMeasureCommand;
import com.spring.framework.recipeapp.repository.reactive.UnitOfMeasureReactiveRepository;
import com.spring.framework.recipeapp.service.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasurementService {

    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(@Autowired UnitOfMeasureReactiveRepository unitOfMeasureRepository,
                                    @Autowired UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {

        return unitOfMeasureRepository.findAll().map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }
}