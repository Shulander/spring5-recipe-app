package us.vicentini.spring5recipeapp.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import us.vicentini.spring5recipeapp.domain.Difficulty;

import java.util.Collection;
import java.util.LinkedList;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class RecipeCommand {
    private Long id;
    private Integer cookTime;
    private Integer prepTime;
    private String description;
    private Difficulty difficulty;
    private String directions;
    private Integer servings;
    private String source;
    private String url;
    private NotesCommand notes;
    private Byte[] image;
    @Builder.Default
    private Collection<CategoryCommand> categories = new LinkedList<>();
    @Builder.Default
    private Collection<IngredientCommand> ingredients = new LinkedList<>();
}
