package us.vicentini.spring5recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {
}
