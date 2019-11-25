package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
                .map(unitOfMeasureToUnitOfMeasureCommand::convert)
                .collect(Collectors.toSet());
    }
}
