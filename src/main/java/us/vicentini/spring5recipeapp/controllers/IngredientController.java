package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.services.IngredientService;
import us.vicentini.spring5recipeapp.services.RecipeService;
import us.vicentini.spring5recipeapp.services.UnitOfMeasureService;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IngredientController {
    private static final String RECIPE_INGREDIENT_INGREDIENTFORM = "recipe/ingredient/ingredientform";
    private static final String INGREDIENT_ATTRIBUTE_NAME = "ingredient";
    private static final String RECIPE_INGREDIENT_SHOW = "recipe/ingredient/show";
    private static final String RECIPE_INGREDIENT_LIST = "recipe/ingredient/list";
    private static final String RECIPE_ATTRIBUTE_NAME = "recipe";

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final UnitOfMeasureService unitOfMeasureService;


    @ModelAttribute("uomList")
    public Flux<UnitOfMeasureCommand> sortedUnitOfMeasureCollection() {
        return getSortedListOfUnitOfMeasure();
    }


    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listRecipeIngredients(@PathVariable String recipeId, Model model) {
        Mono<RecipeCommand> recipe = recipeService.findCommandById(recipeId);
        model.addAttribute(RECIPE_ATTRIBUTE_NAME, recipe);
        return RECIPE_INGREDIENT_LIST;
    }


    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        Mono<IngredientCommand> ingredient = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);
        model.addAttribute(INGREDIENT_ATTRIBUTE_NAME, ingredient);
        return RECIPE_INGREDIENT_SHOW;
    }


    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId,
                                         Model model) {
        Mono<IngredientCommand> ingredient = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);
        model.addAttribute(INGREDIENT_ATTRIBUTE_NAME, ingredient);
        return RECIPE_INGREDIENT_INGREDIENTFORM;
    }


    private Flux<UnitOfMeasureCommand> getSortedListOfUnitOfMeasure() {
        return unitOfMeasureService.listAllUoms()
                .sort((o1, o2) -> Objects.compare(o1.getDescription(), o2.getDescription(), String::compareTo));
    }


    @PostMapping("/recipe/{recipeId}/ingredient")
    public Mono<String> saveOrUpdate(
            @Valid @ModelAttribute(INGREDIENT_ATTRIBUTE_NAME) Mono<IngredientCommand> command) {
        return command.flatMap(ingredientService::saveIngredientCommand)
                .flatMap(savedCommand -> {
                    log.debug("saved receipe id: " + savedCommand.getRecipeId());
                    log.debug("saved ingredient id: " + savedCommand.getId());
                    return Mono.just("redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" +
                                     savedCommand.getId() +
                                     "/show");
                })
                .onErrorResume(throwable -> Mono.just(RECIPE_INGREDIENT_INGREDIENTFORM));
    }


    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public Mono<String> newRecipeIngredient(@PathVariable String recipeId, Model model) {
        return recipeService.findCommandById(recipeId)
                .flatMap(recipeCommand -> {
                    IngredientCommand ingredient = IngredientCommand.builder()
                            .id(UUID.randomUUID().toString())
                            .recipeId(recipeCommand.getId())
                            .build();
                    model.addAttribute(INGREDIENT_ATTRIBUTE_NAME, ingredient);
                    return Mono.just(RECIPE_INGREDIENT_INGREDIENTFORM);
                });
    }


    @GetMapping({"/recipe/{recipeId}/ingredient/{ingredientId}/delete"})
    public Mono<String> delete(@PathVariable String recipeId, @PathVariable String ingredientId) {
        log.info("delete recipe {} ingredient {}", recipeId, ingredientId);
        return ingredientService.deleteByRecipeIdAndIngredientId(recipeId, ingredientId)
                .thenReturn("redirect:/recipe/" + recipeId + "/ingredients");
    }
}
