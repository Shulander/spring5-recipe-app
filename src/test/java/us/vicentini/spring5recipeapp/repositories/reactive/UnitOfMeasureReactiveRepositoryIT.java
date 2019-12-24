package us.vicentini.spring5recipeapp.repositories.reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureReactiveRepositoryIT {
    @Autowired
    private UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;


    @BeforeEach
    public void init() {
        unitOfMeasureReactiveRepository.deleteAll().block();
    }


    @Test
    public void testSave() {
        UnitOfMeasure category = new UnitOfMeasure();
        category.setDescription("Foo");

        unitOfMeasureReactiveRepository.save(category).block();

        Long count = unitOfMeasureReactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }


    @Test
    public void testFindByDescription() {
        UnitOfMeasure category = new UnitOfMeasure();
        category.setDescription("Foo");

        unitOfMeasureReactiveRepository.save(category).then().block();

        UnitOfMeasure fetchedCat = unitOfMeasureReactiveRepository.findByDescription("Foo").block();

        assertNotNull(fetchedCat.getId());
    }
}
