package us.vicentini.spring5recipeapp.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import us.vicentini.spring5recipeapp.bootstrap.BootStrapBaseData;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import(BootStrapBaseData.class)
class UnityOfMeasureRepositoryIT {

    @Autowired
    private UnityOfMeasureRepository unityOfMeasureRepository;


    @Test
    void findByDescriptionTeaspoon() {

        Optional<UnitOfMeasure> unitOfMeasure = unityOfMeasureRepository.findByDescription("Teaspoon");

        assertTrue(unitOfMeasure.isPresent());
        assertEquals("Teaspoon", unitOfMeasure.get().getDescription());
    }


    @Test
    void findByDescriptionCup() {

        Optional<UnitOfMeasure> unitOfMeasure = unityOfMeasureRepository.findByDescription("Cup");

        assertTrue(unitOfMeasure.isPresent());
        assertEquals("Cup", unitOfMeasure.get().getDescription());
    }
}
