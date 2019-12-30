package us.vicentini.spring5recipeapp.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.services.RecipeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
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
        when(recipeService.getRecipes()).thenReturn(Flux.empty());
        String actual = indexController.getIndexPage(model);

        assertEquals("index", actual);
        verify(recipeService).getRecipes();
        verify(model).addAttribute(eq("recipes"), anyList());
    }

    @Test
    void getIndexPageWithArgumentCaptor() {
        List<Recipe> recipes = List.of(Recipe.builder().id("1").build(), Recipe.builder().id("2").build());
        when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));
        ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        String actual = indexController.getIndexPage(model);

        assertEquals("index", actual);
        verify(recipeService).getRecipes();
        verify(model).addAttribute(eq("recipes"), argumentCaptor.capture());
        List<Recipe> actualCapturedRecipes = argumentCaptor.getValue();
        assertEquals(2, actualCapturedRecipes.size());
        assertEquals(recipes, actualCapturedRecipes);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeService,
                                 model);
    }
}
