package us.vicentini.spring5recipeapp.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import us.vicentini.spring5recipeapp.domain.Recipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
