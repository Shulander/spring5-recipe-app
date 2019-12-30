package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.converters.IngredientCommandToIngredient;
import us.vicentini.spring5recipeapp.converters.IngredientToIngredientCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;
import us.vicentini.spring5recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;


    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        IngredientCommand ingredientCommand = IngredientCommand.builder().id(ingredientId).build();
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(buildErrorForRecipeNotFound(recipeId))
                .flatMap(recipe -> findRecipeIngredientCommand(ingredientCommand, recipe));
    }


    private Mono<Recipe> buildErrorForRecipeNotFound(String recipeId) {
        return Mono.error(new NotFoundException("Recipe Not Found for id: " + recipeId));
    }


    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {
        return recipeRepository.findById(ingredientCommand.getRecipeId())
                .switchIfEmpty(buildErrorForRecipeNotFound(ingredientCommand.getRecipeId()))
                .map(recipe -> createOrUpdateIngredient(recipe, ingredientCommand))
                .map(recipeRepository::save)
                .map(Mono::block)
                .flatMap(savedRecipe -> findRecipeIngredientCommand(ingredientCommand, savedRecipe));
    }


    private Recipe createOrUpdateIngredient(Recipe recipe, IngredientCommand ingredientCommand) {
        recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst()
                .ifPresentOrElse(ingredient -> {
                    ingredient.setAmount(ingredientCommand.getAmount());
                    ingredient.setDescription(ingredientCommand.getDescription());
                    ingredient.setUnitOfMeasure(IngredientServiceImpl.this.getById(ingredientCommand));
                }, () -> {
                    Ingredient convert = ingredientCommandToIngredient.convert(ingredientCommand);
                    recipe.addIngredient(convert);
                });
        return recipe;
    }


    @Override
    public Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(buildErrorForRecipeNotFound(recipeId))
                .map(recipe -> findAndRemoveIngredientFromRecipe(recipe, ingredientId))
                .flatMap(recipeRepository::save)
                .flatMap(recipe -> Mono.empty());
    }


    private Recipe findAndRemoveIngredientFromRecipe(Recipe recipe, String ingredientId) {
        if (!recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientId))) {
            throw new NotFoundException("Ingredient Not Found for id: " + ingredientId);
        }
        return recipe;
    }


    private Mono<IngredientCommand> findRecipeIngredientCommand(IngredientCommand ingredientCommand, Recipe recipe) {
        return Mono.just(recipe)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> isSameIngredient(ingredientCommand, ingredient))
                .singleOrEmpty()
                .switchIfEmpty(buildErrorForIngredientNotFound(ingredientCommand))
                .map(ingredientToIngredientCommand::convert)
                .map(ingredient -> {
                    ingredient.setRecipeId(recipe.getId());
                    return ingredient;
                });
    }


    private Mono<Ingredient> buildErrorForIngredientNotFound(IngredientCommand ingredientCommand) {
        return Mono.error(new NotFoundException("Ingredient Not Found for id: " + ingredientCommand.getId()));
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
        return unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId()).block();
    }
}
