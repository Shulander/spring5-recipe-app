package us.vicentini.spring5recipeapp.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import us.vicentini.spring5recipeapp.recipe.RecipeService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @InjectMocks
    private IndexController indexController;

    @Mock
    private RecipeService recipeService;
    @Mock
    private Model model;


    @Test
    void getIndexPage() {
        String actual = indexController.getIndexPage(model);

        assertEquals("index", actual);
        verify(recipeService).getRecipes();
        verify(model).addAttribute(eq("recipes"), anySet());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeService,
                                 model);
    }
}
