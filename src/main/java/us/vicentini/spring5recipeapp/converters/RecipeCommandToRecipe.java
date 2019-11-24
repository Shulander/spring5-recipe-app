package us.vicentini.spring5recipeapp.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;

@Component
@RequiredArgsConstructor
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {
    private final CategoryCommandToCategory categoryCommandToCategory;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final NotesCommandToNotes notesCommandToNotes;

    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        if (recipeCommand == null) {
            return null;
        }
        Recipe returnRecipe = Recipe.builder()
                .id(recipeCommand.getId())
                .cookTime(recipeCommand.getCookTime())
                .prepTime(recipeCommand.getPrepTime())
                .description(recipeCommand.getDescription())
                .difficulty(recipeCommand.getDifficulty())
                .directions(recipeCommand.getDirections())
                .servings(recipeCommand.getServings())
                .source(recipeCommand.getSource())
                .url(recipeCommand.getUrl())
                .notes(notesCommandToNotes.convert(recipeCommand.getNotes()))
                .build();

        recipeCommand.getCategories()
                .forEach(categoryCommand -> returnRecipe
                        .addCategory(categoryCommandToCategory.convert(categoryCommand)));
        recipeCommand.getIngredients()
                .forEach(ingredientCommand -> returnRecipe
                        .addIngredient(ingredientCommandToIngredient.convert(ingredientCommand)));

        return returnRecipe;
    }
}
