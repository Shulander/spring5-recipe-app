package us.vicentini.spring5recipeapp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UnityOfMeasureRepositoryITTest {

    @Autowired
    private UnityOfMeasureRepository unityOfMeasureRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

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
