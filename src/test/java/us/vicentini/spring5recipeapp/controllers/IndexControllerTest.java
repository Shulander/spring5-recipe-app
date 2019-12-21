package us.vicentini.spring5recipeapp.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.services.RecipeService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

    @Test
    void getIndexPageWithArgumentCaptor() {
        Set<Recipe> recipes = Set.of(Recipe.builder().id("1").build(), Recipe.builder().id("2").build());
        when(recipeService.getRecipes()).thenReturn(recipes);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        String actual = indexController.getIndexPage(model);

        assertEquals("index", actual);
        verify(recipeService).getRecipes();
        verify(model).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> actualCapturedRecipes = argumentCaptor.getValue();
        assertEquals(2, actualCapturedRecipes.size());
        assertEquals(recipes, actualCapturedRecipes);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeService,
                                 model);
    }
}
