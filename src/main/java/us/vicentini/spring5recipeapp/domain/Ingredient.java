package us.vicentini.spring5recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"recipe"})
@ToString(exclude = "recipe")
@AllArgsConstructor(access = PRIVATE)
public class Ingredient {
    private String id;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasure unitOfMeasure;
    private Recipe recipe;
}
