package us.vicentini.spring5recipeapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.converters.RecipeCommandToRecipe;
import us.vicentini.spring5recipeapp.converters.RecipeToRecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RecipeServiceImplTest {
    private static final String ENTITY_ID = "1";

    @Mock
    private RecipeReactiveRepository recipeRepository;
    @Mock
    private RecipeCommandToRecipe recipeCommandToRecipe;
    @Mock
    private RecipeToRecipeCommand recipeToRecipeCommand;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }


    @Test
    void getRecipesFindById() {
        Recipe recipe = mock(Recipe.class);
        when(recipeRepository.findById(ENTITY_ID)).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById(ENTITY_ID).block();

        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeRepository).findById(ENTITY_ID);
    }


    @Test
    public void getRecipeByIdTestNotFound() {
        Mono<Recipe> recipeOptional = Mono.empty();
        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> recipeService.findById(ENTITY_ID).block());

        assertNotNull(exception);
        assertEquals("Recipe Not Found for id: 1", exception.getMessage());
        verify(recipeRepository).findById(ENTITY_ID);
    }


    @Test
    void getRecipesFindByIdNotFound() {
        when(recipeRepository.findById(ENTITY_ID)).thenReturn(Mono.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> recipeService.findById(ENTITY_ID).block());

        assertEquals("Recipe Not Found for id: 1", ex.getMessage());
        verify(recipeRepository).findById(ENTITY_ID);
    }

    @Test
    void getRecipes() {
        Recipe recipe = mock(Recipe.class);
        when(recipeRepository.findAll()).thenReturn(Flux.just(recipe));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertNotNull(recipes);
        assertFalse(recipes.isEmpty());
        assertEquals(recipe, recipes.iterator().next());
        verify(recipeRepository).findAll();
    }

    @Test
    void getRecipesCommandFindById() {
        Recipe recipe = mock(Recipe.class);
        RecipeCommand recipeCommand = mock(RecipeCommand.class);
        when(recipeRepository.findById(ENTITY_ID)).thenReturn(Mono.just(recipe));
        when(recipeToRecipeCommand.convert(recipe)).thenReturn(recipeCommand);

        RecipeCommand recipeReturned = recipeService.findCommandById(ENTITY_ID).block();

        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeRepository).findById(ENTITY_ID);
        verify(recipeToRecipeCommand).convert(recipe);
    }

    @Test
    void testDeleteById() {
        when(recipeRepository.deleteById(ENTITY_ID)).thenReturn(Mono.empty());

        recipeService.deleteById(ENTITY_ID).block();

        verify(recipeRepository).deleteById(ENTITY_ID);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeRepository);
    }
}
