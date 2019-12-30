package us.vicentini.spring5recipeapp.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@ToString(exclude = "recipes")
@EqualsAndHashCode(exclude = "recipes")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class Category {

    @Id
    private String id;
    private String description;
    private Set<Recipe> recipes;
}
