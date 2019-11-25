package us.vicentini.spring5recipeapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.converters.IngredientCommandToIngredient;
import us.vicentini.spring5recipeapp.converters.IngredientToIngredientCommand;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.repositories.RecipeRepository;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private IngredientServiceImpl ingredientService;

    IngredientServiceImplTest() {
        this.ingredientToIngredientCommand =
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient =
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    void init() {
        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand,
                                                      ingredientCommandToIngredient,
                                                      recipeRepository,
                                                      unitOfMeasureRepository);
    }

    @Test
    void findByRecipeIdAndRecipeIdHappyPath() {
        //given
        Recipe recipe = Recipe.builder().id(1L).build();
        recipe.addIngredient(Ingredient.builder().id(1L).build());
        recipe.addIngredient(Ingredient.builder().id(2L).build());
        recipe.addIngredient(Ingredient.builder().id(3L).build());

        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

        //when
        assertEquals(3L, ingredientCommand.getId());
        assertEquals(1L, ingredientCommand.getRecipeId());
        verify(recipeRepository).findById(anyLong());
    }

    @Test
    void findByRecipeIdAndRecipeIdFailRecipeNotfound() {
        //given
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> ingredientService.findByRecipeIdAndIngredientId(1L, 3L));

        //when
        assertEquals("Recipe Not Found for id: 1", ex.getMessage());
        verify(recipeRepository).findById(anyLong());
    }

    @Test
    void findByRecipeIdAndRecipeIdFailIngredientNotfound() {
        //given
        Recipe recipe = Recipe.builder().id(1L).build();
        recipe.addIngredient(Ingredient.builder().id(1L).build());
        recipe.addIngredient(Ingredient.builder().id(2L).build());

        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        //then
        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> ingredientService.findByRecipeIdAndIngredientId(1L, 3L));

        //when
        assertEquals("Ingredient Not Found for id: 3", ex.getMessage());
        verify(recipeRepository).findById(anyLong());
    }

    @Test
    public void testSaveRecipeCommand() throws Exception {
        //given
        IngredientCommand command = IngredientCommand.builder()
                .id(3L)
                .recipeId(2L)
                .build();

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = Recipe.builder().id(2L).build();
        savedRecipe.addIngredient(Ingredient.builder().id(3L).build());

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        //then
        assertEquals(Long.valueOf(3L), savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }


    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeRepository,
                                 unitOfMeasureRepository);
    }
}
