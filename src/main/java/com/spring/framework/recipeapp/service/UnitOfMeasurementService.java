package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.command.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasurementService {

    Flux<UnitOfMeasureCommand> listAllUoms();
}
