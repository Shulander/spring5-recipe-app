package us.vicentini.spring5recipeapp.repository;

import org.springframework.data.repository.CrudRepository;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

public interface UnityOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {
}
