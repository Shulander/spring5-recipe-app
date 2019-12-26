package us.vicentini.spring5recipeapp.services;

import reactor.core.publisher.Flux;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> listAllUoms();
}
