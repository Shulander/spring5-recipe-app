package us.vicentini.spring5recipeapp.repository;

import org.springframework.data.repository.CrudRepository;
import us.vicentini.spring5recipeapp.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
