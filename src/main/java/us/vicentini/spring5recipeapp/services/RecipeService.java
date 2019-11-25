package us.vicentini.spring5recipeapp.services;

import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe findById(long id);

    RecipeCommand saveRecipeCommand(RecipeCommand testRecipeCommand);

    RecipeCommand findCommandById(Long id);
}
