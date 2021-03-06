package us.vicentini.spring5recipeapp.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import us.vicentini.spring5recipeapp.domain.Difficulty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class RecipeCommand {
    private String id;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;
    private Difficulty difficulty;

    @NotBlank
    private String directions;

    @Min(1)
    @Max(100)
    private Integer servings;
    private String source;

    @URL
    private String url;
    private NotesCommand notes;
    private Byte[] image;
    @Builder.Default
    private List<CategoryCommand> categories = new ArrayList<>();
    @Builder.Default
    private List<IngredientCommand> ingredients = new ArrayList<>();
}
