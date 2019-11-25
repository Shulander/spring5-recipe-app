package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.converters.IngredientCommandToIngredient;
import us.vicentini.spring5recipeapp.converters.IngredientToIngredientCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.repositories.RecipeRepository;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe Not Found for id: " + recipeId))
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst()
                .map(ingredientToIngredientCommand::convert)
                .orElseThrow(() -> new RuntimeException("Ingredient Not Found for id: " + ingredientId));
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe Not Found for id: " + ingredientCommand.getRecipeId()));

        Optional<Ingredient> ingredientOptional = recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredient = ingredientOptional.get();
            ingredient.setAmount(ingredientCommand.getAmount());
            ingredient.setDescription(ingredientCommand.getDescription());
            ingredient.setUnitOfMeasure(getById(ingredientCommand));
        } else {
            Ingredient convert = ingredientCommandToIngredient.convert(ingredientCommand);
            recipe.addIngredient(convert);
        }

        return recipeRepository.save(recipe)
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst()
                .map(ingredientToIngredientCommand::convert)
                .orElseThrow(() -> new RuntimeException("Ingredient Not Found for id: " + ingredientCommand.getId()));
    }

    private UnitOfMeasure getById(IngredientCommand ingredientCommand) {
        if (ingredientCommand.getUnitOfMeasure() == null) {
            return null;
        }
        return unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId())
                .orElseThrow();
    }
}
