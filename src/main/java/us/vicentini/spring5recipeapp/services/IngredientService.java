package us.vicentini.spring5recipeapp.services;

import us.vicentini.spring5recipeapp.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}