package us.vicentini.spring5recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {
    Optional<UnitOfMeasure> findByDescription(String description);
}
