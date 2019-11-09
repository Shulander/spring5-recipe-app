package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import us.vicentini.spring5recipeapp.domain.Category;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.repository.CategoryRepository;
import us.vicentini.spring5recipeapp.repository.UnityOfMeasureRepository;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final CategoryRepository categoryRepository;
    private final UnityOfMeasureRepository unityOfMeasureRepository;

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage() {
        log.info("redirecting to index page");
        Optional<Category> categoryOptional = categoryRepository.findByDescription("Italian");
        Optional<UnitOfMeasure> unityOfMeasureOptional = unityOfMeasureRepository.findByDescription("Teaspoon");
        log.info("Cat Id is: {}", categoryOptional.get().getId());
        log.info("UOM Id is: {}", unityOfMeasureOptional.get().getId());
        return "index";
    }
}
