package us.vicentini.spring5recipeapp.repository;

import org.springframework.data.repository.CrudRepository;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

import java.util.Optional;

public interface UnityOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByDescription(String description);
}
