package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.converters.RecipeCommandToRecipe;
import us.vicentini.spring5recipeapp.converters.RecipeToRecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.RecipeReactiveRepository;

@Service
@RequiredArgsConstructor
class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeRepository;

    private final RecipeCommandToRecipe recipeCommandToRecipe;

    private final RecipeToRecipeCommand recipeToRecipeCommand;


    @Override
    public Flux<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }


    @Override
    public Mono<Recipe> findById(String id) {
        return recipeRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe Not Found for id: " + id)));
    }


    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand testRecipeCommand) {
        return Mono.just(testRecipeCommand)
                .map(recipeCommandToRecipe::convert)
                .flatMap(recipeRepository::save)
                .map(recipeToRecipeCommand::convert);
    }


    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return Mono.just(id)
                .flatMap(this::findById)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe Not Found for id: " + id)))
                .map(recipeToRecipeCommand::convert)
                .map(recipeCommand -> {
                    recipeCommand.getIngredients()
                            .forEach(ingredient -> ingredient.setRecipeId(recipeCommand.getId()));
                    return recipeCommand;
                });
    }


    @Override
    public Mono<Void> deleteById(String id) {
        return recipeRepository.deleteById(id);
    }
}
