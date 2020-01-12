package us.vicentini.spring5recipeapp.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.domain.Category;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.repositories.CategoryReactiveRepository;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureReactiveRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootStrapBaseData implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryReactiveRepository categoryRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (categoryRepository.count().block() == 0L) {
            log.debug("Loading Categories");
            loadCategories();
        }

        if (unitOfMeasureRepository.count().block() == 0L) {
            log.debug("Loading UOMs");
            loadUom();
        }
    }


    private void loadCategories() {
        Category cat1 = Category.builder().description("American").build();
        categoryRepository.save(cat1).block();

        Category cat2 = Category.builder().description("Italian").build();
        categoryRepository.save(cat2).block();

        Category cat3 = Category.builder().description("Mexican").build();
        categoryRepository.save(cat3).block();

        Category cat4 = Category.builder().description("Fast Food").build();
        categoryRepository.save(cat4).block();
    }


    private void loadUom() {
        UnitOfMeasure uom1 = UnitOfMeasure.builder().description("Teaspoon").build();
        unitOfMeasureRepository.save(uom1).block();

        UnitOfMeasure uom2 = UnitOfMeasure.builder().description("Tablespoon").build();
        unitOfMeasureRepository.save(uom2).block();

        UnitOfMeasure uom3 = UnitOfMeasure.builder().description("Cup").build();
        unitOfMeasureRepository.save(uom3).block();

        UnitOfMeasure uom4 = UnitOfMeasure.builder().description("Pinch").build();
        unitOfMeasureRepository.save(uom4).block();

        UnitOfMeasure uom5 = UnitOfMeasure.builder().description("Ounce").build();
        unitOfMeasureRepository.save(uom5).block();

        UnitOfMeasure uom6 = UnitOfMeasure.builder().description("Each").build();
        unitOfMeasureRepository.save(uom6).block();

        UnitOfMeasure uom7 = UnitOfMeasure.builder().description("Pint").build();
        unitOfMeasureRepository.save(uom7).block();

        UnitOfMeasure uom8 = UnitOfMeasure.builder().description("Dash").build();
        unitOfMeasureRepository.save(uom8).block();
    }
}
