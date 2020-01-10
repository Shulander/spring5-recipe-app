package us.vicentini.spring5recipeapp.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IngredientCommand {
    private String id;
    private String recipeId;

    @NotNull
    @Min(1)
    private BigDecimal amount;
    @NotBlank
    private String description;
    @NotNull
    private UnitOfMeasureCommand unitOfMeasure;
}
