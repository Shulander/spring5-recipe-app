package us.vicentini.spring5recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode()
@ToString()
@AllArgsConstructor(access = PRIVATE)
@Document
public class Ingredient {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;
    @DBRef
    private UnitOfMeasure unitOfMeasure;
}
