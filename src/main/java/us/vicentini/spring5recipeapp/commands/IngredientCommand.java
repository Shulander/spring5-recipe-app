package us.vicentini.spring5recipeapp.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IngredientCommand {
    private String id;
    //todo: remove this property
    private String recipeId;
    private BigDecimal amount;
    private String description;
    private UnitOfMeasureCommand unitOfMeasure;
}
