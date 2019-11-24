package us.vicentini.spring5recipeapp.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;

@Component
@RequiredArgsConstructor
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {
    private final CategoryToCategoryCommand categoryToCategoryCommand;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final NotesToNotesCommand notesToNotesCommand;

    @Override
    public RecipeCommand convert(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        RecipeCommand returnRecipe = RecipeCommand.builder()
                .id(recipe.getId())
                .cookTime(recipe.getCookTime())
                .prepTime(recipe.getPrepTime())
                .description(recipe.getDescription())
                .difficulty(recipe.getDifficulty())
                .directions(recipe.getDirections())
                .servings(recipe.getServings())
                .source(recipe.getSource())
                .url(recipe.getUrl())
                .notes(notesToNotesCommand.convert(recipe.getNotes()))
                .build();

        recipe.getCategories()
                .forEach(categoryCommand -> returnRecipe.getCategories()
                        .add(categoryToCategoryCommand.convert(categoryCommand)));
        recipe.getIngredients()
                .forEach(ingredientCommand -> returnRecipe.getIngredients()
                        .add(ingredientToIngredientCommand.convert(ingredientCommand)));

        return returnRecipe;
    }
}
