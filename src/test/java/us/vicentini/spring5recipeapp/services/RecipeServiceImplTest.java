package us.vicentini.spring5recipeapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.converters.RecipeCommandToRecipe;
import us.vicentini.spring5recipeapp.converters.RecipeToRecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.RecipeRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;
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
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        Recipe recipeReturned = recipeService.findById(1L);

        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeRepository).findById(1L);
    }


    @Test
    public void getRecipeByIdTestNotFound() {
        Optional<Recipe> recipeOptional = Optional.empty();
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> recipeService.findById(1L));

        assertNotNull(exception);
        assertEquals("Recipe Not Found for id: 1", exception.getMessage());
        verify(recipeRepository).findById(1L);
    }


    @Test
    void getRecipesFindByIdNotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> recipeService.findById(1L));

        assertEquals("Recipe Not Found for id: 1", ex.getMessage());
        verify(recipeRepository).findById(1L);
    }

    @Test
    void getRecipes() {
        Recipe recipe = mock(Recipe.class);
        when(recipeRepository.findAll()).thenReturn(Collections.singletonList(recipe));

        Set<Recipe> recipes = recipeService.getRecipes();

        assertNotNull(recipes);
        assertFalse(recipes.isEmpty());
        assertEquals(recipe, recipes.iterator().next());
        verify(recipeRepository).findAll();
    }

    @Test
    void getRecipesCommandFindById() {
        Recipe recipe = mock(Recipe.class);
        RecipeCommand recipeCommand = mock(RecipeCommand.class);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeToRecipeCommand.convert(recipe)).thenReturn(recipeCommand);

        RecipeCommand recipeReturned = recipeService.findCommandById(1L);

        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeRepository).findById(1L);
        verify(recipeToRecipeCommand).convert(recipe);
    }

    @Test
    void testDeleteById() {
        recipeService.deleteById(1L);

        verify(recipeRepository).deleteById(1L);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeRepository);
    }
}
