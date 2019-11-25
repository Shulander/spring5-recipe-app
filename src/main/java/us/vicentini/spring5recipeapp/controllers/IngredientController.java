package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.services.RecipeService;

@Controller
@RequiredArgsConstructor
public class IngredientController {

    private final RecipeService recipeService;

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String showRecipe(@PathVariable Long recipeId, Model model) {
        RecipeCommand recipe = recipeService.findCommandById(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipe/ingredient/list";
    }
}
