package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.command.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasurementService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
