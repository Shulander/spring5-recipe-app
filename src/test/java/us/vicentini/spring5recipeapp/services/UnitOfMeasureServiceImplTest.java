package us.vicentini.spring5recipeapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UnitOfMeasureServiceImplTest {

    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand =
            new UnitOfMeasureToUnitOfMeasureCommand();
    private UnitOfMeasureService service;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {
        //given
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        unitOfMeasures.add(UnitOfMeasure.builder().id(1L).build());
        unitOfMeasures.add(UnitOfMeasure.builder().id(2L).build());

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        //when
        Set<UnitOfMeasureCommand> commands = service.listAllUoms();

        //then
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository).findAll();
    }

}
