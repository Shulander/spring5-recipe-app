package us.vicentini.spring5recipeapp.services;

import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;

public interface IngredientService {

    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand);

    Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId);
}
