package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.services.RecipeService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private static final String RECIPE_RECIPE_FORM = "recipe/recipeform";

    private final RecipeService recipeService;


    @GetMapping({"/{id}/show"})
    public String showRecipe(@PathVariable String id, Model model) {
        Mono<RecipeCommand> recipe = recipeService.findCommandById(id);
        model.addAttribute("recipe", recipe);
        return "recipe/show";
    }


    @GetMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPE_FORM;
    }


    @GetMapping("/{id}/update")
    public String newRecipe(@PathVariable String id, Model model) {
        Mono<RecipeCommand> recipe = recipeService.findCommandById(id);
        model.addAttribute("recipe", recipe);

        return RECIPE_RECIPE_FORM;
    }


    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RECIPE_RECIPE_FORM;
        }
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command).block();

        return "redirect:/recipe/" + savedCommand.getId() + "/show/";
    }


    @GetMapping({"/{id}/delete"})
    public String delete(@PathVariable String id) {
        recipeService.deleteById(id).block();

        return "redirect:/index";
    }
}
