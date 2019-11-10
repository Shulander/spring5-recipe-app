package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import us.vicentini.spring5recipeapp.recipe.RecipeService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RecipeService recipeService;

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        log.info("redirecting to index page");

        model.addAttribute("recipes", recipeService.getRecipes());

        return "index";
    }
}
