package us.vicentini.spring5recipeapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.converters.IngredientCommandToIngredient;
import us.vicentini.spring5recipeapp.converters.IngredientToIngredientCommand;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.repositories.RecipeRepository;
import us.vicentini.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    public void testUpdateRecipeCommand() throws Exception {
        //given
        IngredientCommand command = IngredientCommand.builder()
                .id(3L)
                .recipeId(2L)
                .amount(BigDecimal.ONE)
                .description("description")
                .unitOfMeasure(UnitOfMeasureCommand.builder()
                                       .id(1L)
                                       .description("uom")
                                       .build())
                .build();

        Recipe recipe = Recipe.builder()
                .id(2L)
                .build();
        recipe.addIngredient(Ingredient.builder().id(1L).build());
        recipe.addIngredient(Ingredient.builder().id(2L).build());
        recipe.addIngredient(Ingredient.builder().id(3L).build());
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        Recipe savedRecipe = Recipe.builder().id(2L).build();
        savedRecipe.addIngredient(Ingredient.builder().id(3L).build());

        UnitOfMeasure uom = UnitOfMeasure.builder().id(1L).description("uom").build();
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);
        when(unitOfMeasureRepository.findById(1L)).thenReturn(Optional.of(uom));

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        //then
        assertEquals(Long.valueOf(3L), savedCommand.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
        verify(unitOfMeasureRepository).findById(1L);

    }

    @Test
    public void testSaveNewRecipeCommand() throws Exception {
        //given
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .recipeId(2L)
                .amount(BigDecimal.ONE)
                .description("description")
                .unitOfMeasure(UnitOfMeasureCommand.builder()
                                       .id(1L)
                                       .build())
                .build();
        Optional<Recipe> recipeOptional = Optional.of(Recipe.builder().id(2L).build());

        Recipe savedRecipe = Recipe.builder().id(2L).build();
        Ingredient ingredient = Ingredient.builder()
                .id(3L)
                .amount(BigDecimal.ONE)
                .description("description")
                .unitOfMeasure(UnitOfMeasure.builder()
                                       .id(1L)
                                       .description("uom")
                                       .build())
                .build();
        savedRecipe.addIngredient(ingredient);

        when(recipeRepository.findById(2L)).thenReturn(recipeOptional);
        when(recipeRepository.save(recipeOptional.get())).thenReturn(savedRecipe);

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        //then
        assertEquals(Long.valueOf(3L), savedCommand.getId());
        assertEquals(ingredientCommand.getAmount(), savedCommand.getAmount());
        assertEquals(ingredientCommand.getDescription(), savedCommand.getDescription());
        assertEquals(ingredientCommand.getUnitOfMeasure().getId(), savedCommand.getUnitOfMeasure().getId());
        verify(recipeRepository).findById(2L);
        verify(recipeRepository).save(any(Recipe.class));
    }


    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeRepository,
                                 unitOfMeasureRepository);
    }
}
