package us.vicentini.spring5recipeapp.repository;

import org.springframework.data.repository.CrudRepository;
import us.vicentini.spring5recipeapp.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
