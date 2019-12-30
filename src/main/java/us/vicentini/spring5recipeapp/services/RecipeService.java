package us.vicentini.spring5recipeapp.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;

public interface RecipeService {
    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(String id);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand testRecipeCommand);

    Mono<RecipeCommand> findCommandById(String id);

    Mono<Void> deleteById(String id);
}
