package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.converters.IngredientCommandToIngredient;
import us.vicentini.spring5recipeapp.converters.IngredientToIngredientCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.RecipeReactiveRepository;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureReactiveRepository;

import java.util.Objects;
import java.util.Optional;

@Slf4j
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
                .flatMap(recipe -> createOrUpdateIngredient(recipe, ingredientCommand))
                .flatMap(recipeRepository::save)
                .flatMap(savedRecipe -> findRecipeIngredientCommand(ingredientCommand, savedRecipe));
    }


    private Mono<Recipe> createOrUpdateIngredient(Recipe recipe, IngredientCommand ingredientCommand) {
        Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();
        return optionalIngredient
                .map(ingredient -> updateExistingIngredient(recipe, ingredientCommand, ingredient))
                .orElseGet(() -> createNewIngredient(recipe, ingredientCommand));

    }


    private Mono<Recipe> createNewIngredient(Recipe recipe, IngredientCommand ingredientCommand) {
        return Mono.just(ingredientCommand)
                .map(ingredientCommandToIngredient::convert)
                .flatMap(ingredient -> updateUOMFromDB(ingredientCommand, ingredient))
                .flatMap(ingredient -> {
                    recipe.addIngredient(ingredient);
                    return Mono.just(recipe);
                });
    }


    private Mono<Recipe> updateExistingIngredient(Recipe recipe, IngredientCommand ingredientCommand,
                                                  Ingredient ingredient) {
        return Mono.just(ingredient)
                .flatMap(ingredient1 -> {
                    ingredient1.setAmount(ingredientCommand.getAmount());
                    ingredient1.setDescription(ingredientCommand.getDescription());
                    return Mono.just(ingredient1);
                })
                .flatMap(ingredient1 -> updateUOMFromDB(ingredientCommand, ingredient1))
                .flatMap(ingredient1 -> Mono.just(recipe));
    }


    private Mono<Ingredient> updateUOMFromDB(IngredientCommand ingredientCommand, Ingredient ingredient) {
        return getById(ingredientCommand)
                .flatMap(unitOfMeasure -> {
                    ingredient.setUnitOfMeasure(unitOfMeasure);
                    return Mono.just(ingredient);
                });
    }


    @Override
    public Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(buildErrorForRecipeNotFound(recipeId))
                .flatMap(recipe -> findAndRemoveIngredientFromRecipe(recipe, ingredientId))
                .flatMap(recipeRepository::save)
                .flatMap(recipe -> Mono.empty());
    }


    private Mono<Recipe> findAndRemoveIngredientFromRecipe(Recipe recipe, String ingredientId) {
        if (!recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientId))) {
            return Mono.error(new NotFoundException("Ingredient Not Found for id: " + ingredientId));
        }
        return Mono.just(recipe);
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


    private Mono<UnitOfMeasure> getById(IngredientCommand ingredientCommand) {
        if (ingredientCommand.getUnitOfMeasure() == null) {
            return Mono.empty();
        }
        return unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId());
    }
}
