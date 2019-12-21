package us.vicentini.spring5recipeapp.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;

@Component
@RequiredArgsConstructor
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Override
    public IngredientCommand convert(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return IngredientCommand.builder()
                .id(ingredient.getId())
                .recipeId(getRecipeId(ingredient))
                .amount(ingredient.getAmount())
                .description(ingredient.getDescription())
                .unitOfMeasure(unitOfMeasureToUnitOfMeasureCommand.convert(ingredient.getUnitOfMeasure()))
                .build();
    }

    private String getRecipeId(Ingredient ingredient) {
        return ingredient.getRecipe() == null ? null : ingredient.getRecipe().getId();
    }
}
