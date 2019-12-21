package us.vicentini.spring5recipeapp.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"ingredients", "categories"})
@NoArgsConstructor
@ToString(exclude = {"ingredients", "categories"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Recipe {
    private String id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Difficulty difficulty;
    private Byte[] image;
    private Notes notes;
    @Builder.Default
    private Set<Ingredient> ingredients = new HashSet<>();
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

    public void setNotes(Notes notes) {
        if (notes != null) {
            notes.setRecipe(this);
            this.notes = notes;
        }
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            ingredient.setRecipe(this);
            ingredients.add(ingredient);
        }
    }

    public void addCategory(Category category) {
        if (category != null) {
            categories.add(category);
        }
    }
}
