package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.services.RecipeService;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @RequestMapping({"/{id}/show"})
    public String showRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);
        return "recipe/show";
    }

    @RequestMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @RequestMapping("/{id}/update")
    public String newRecipe(@PathVariable Long id, Model model) {
        RecipeCommand recipe = recipeService.findCommandById(id);
        model.addAttribute("recipe", recipe);

        return "recipe/recipeform";
    }

    @PostMapping({"/"})
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/" + savedCommand.getId() + "/show/";
    }
}
