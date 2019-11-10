package us.vicentini.spring5recipeapp.recipe;

import us.vicentini.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
