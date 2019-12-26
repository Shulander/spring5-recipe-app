package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.services.IngredientService;
import us.vicentini.spring5recipeapp.services.RecipeService;
import us.vicentini.spring5recipeapp.services.UnitOfMeasureService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listRecipeIngredients(@PathVariable String recipeId, Model model) {
        RecipeCommand recipe = recipeService.findCommandById(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        IngredientCommand ingredient = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId).block();
        model.addAttribute("ingredient", ingredient);
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        IngredientCommand ingredient = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId).block();
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("uomList", getSortedListOfUnitOfMeasure());
        return "recipe/ingredient/ingredientform";
    }


    private List<UnitOfMeasureCommand> getSortedListOfUnitOfMeasure() {
        return unitOfMeasureService.listAllUoms()
                .sort((o1, o2) -> Objects.compare(o1.getDescription(), o2.getDescription(), String::compareTo))
                .collectList()
                .block();
    }


    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        log.debug("saved receipe id: " + savedCommand.getRecipeId());
        log.debug("saved ingredient id: " + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
        //todo raise exception if null

        IngredientCommand ingredient = IngredientCommand.builder()
                .id(UUID.randomUUID().toString())
                .recipeId(recipeCommand.getId())
                .build();
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("uomList", getSortedListOfUnitOfMeasure());

        return "recipe/ingredient/ingredientform";
    }


    @GetMapping({"/recipe/{recipeId}/ingredient/{ingredientId}/delete"})
    public String delete(@PathVariable String recipeId, @PathVariable String ingredientId) {
        log.info("delete recipe {} ingredient {}", recipeId, ingredientId);
        ingredientService.deleteByRecipeIdAndIngredientId(recipeId, ingredientId).block();

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
