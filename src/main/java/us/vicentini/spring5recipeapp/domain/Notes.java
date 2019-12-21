package us.vicentini.spring5recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@ToString(exclude = "recipe")
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class Notes {
    private String id;
    private Recipe recipe;
    private String recipeNotes;
}
