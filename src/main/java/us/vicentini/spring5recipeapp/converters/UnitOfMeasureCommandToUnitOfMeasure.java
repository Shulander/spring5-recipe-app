package us.vicentini.spring5recipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand uomCommand) {
        if (uomCommand == null) {
            return null;
        }
        return UnitOfMeasure.builder()
                .id(uomCommand.getId())
                .description(uomCommand.getDescription())
                .build();
    }
}
