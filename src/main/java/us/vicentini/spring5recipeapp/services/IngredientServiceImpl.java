package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.converters.IngredientCommandToIngredient;
import us.vicentini.spring5recipeapp.converters.IngredientToIngredientCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.RecipeRepository;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe Not Found for id: " + recipeId));
        return findRecipeIngredientCommand(IngredientCommand.builder().id(ingredientId).build(), recipe);
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId())
                .orElseThrow(
                        () -> new NotFoundException("Recipe Not Found for id: " + ingredientCommand.getRecipeId()));

        recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst()
                .ifPresentOrElse(ingredient -> {
                    ingredient.setAmount(ingredientCommand.getAmount());
                    ingredient.setDescription(ingredientCommand.getDescription());
                    ingredient.setUnitOfMeasure(getById(ingredientCommand));
                }, () -> {
                    Ingredient convert = ingredientCommandToIngredient.convert(ingredientCommand);
                    recipe.addIngredient(convert);
                });

        Recipe savedRecipe = recipeRepository.save(recipe);
        return findRecipeIngredientCommand(ingredientCommand, savedRecipe);
    }

    @Override
    public void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe Not Found for id: " + recipeId));

        if (!recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientId))) {
            throw new NotFoundException("Ingredient Not Found for id: " + ingredientId);
        }

        recipeRepository.save(recipe);
    }

    private IngredientCommand findRecipeIngredientCommand(IngredientCommand ingredientCommand, Recipe recipe) {
        return recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> isSameIngredient(ingredientCommand, ingredient))
                .findFirst()
                .map(ingredientToIngredientCommand::convert)
                .orElseThrow(() -> new NotFoundException("Ingredient Not Found for id: " + ingredientCommand.getId()));
    }

    private boolean isSameIngredient(IngredientCommand ingredientCommand, Ingredient ingredient) {
        if (ingredientCommand.getId() != null) {
            return ingredient.getId().equals(ingredientCommand.getId());
        } else {
            return Objects.equals(ingredient.getDescription(), ingredientCommand.getDescription())
                   && Objects.equals(ingredient.getAmount(), ingredientCommand.getAmount())
                   && Objects.equals(ingredient.getUnitOfMeasure().getId(),
                                     ingredientCommand.getUnitOfMeasure().getId());
        }
    }

    private UnitOfMeasure getById(IngredientCommand ingredientCommand) {
        if (ingredientCommand.getUnitOfMeasure() == null) {
            return null;
        }
        return unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId())
                .orElseThrow();
    }
}
