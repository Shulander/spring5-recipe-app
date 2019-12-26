package us.vicentini.spring5recipeapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.IngredientCommand;
import us.vicentini.spring5recipeapp.commands.UnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.converters.IngredientCommandToIngredient;
import us.vicentini.spring5recipeapp.converters.IngredientToIngredientCommand;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import us.vicentini.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import us.vicentini.spring5recipeapp.domain.Ingredient;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.domain.UnitOfMeasure;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;
import us.vicentini.spring5recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {
    private static final String ENTITY_ID = "1";

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    private RecipeReactiveRepository recipeRepository;
    @Mock
    private UnitOfMeasureReactiveRepository unitOfMeasureRepository;

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
        Recipe recipe = Recipe.builder().id(ENTITY_ID).build();
        recipe.addIngredient(Ingredient.builder().id(ENTITY_ID).build());
        recipe.addIngredient(Ingredient.builder().id("2").build());
        recipe.addIngredient(Ingredient.builder().id("3").build());

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(ENTITY_ID, "3").block();

        //when
        assertEquals("3", ingredientCommand.getId());
        verify(recipeRepository).findById(anyString());
    }

    @Test
    void findByRecipeIdAndRecipeIdFailRecipeNotfound() {
        //given
        when(recipeRepository.findById(anyString())).thenReturn(Mono.empty());

        //then
        NotFoundException ex =
                assertThrows(NotFoundException.class, () -> ingredientService.findByRecipeIdAndIngredientId(ENTITY_ID
                        , "3").block());

        //when
        assertEquals("Recipe Not Found for id: 1", ex.getMessage());
        verify(recipeRepository).findById(anyString());
    }

    @Test
    void findByRecipeIdAndRecipeIdFailIngredientNotfound() {
        //given
        Recipe recipe = Recipe.builder().id(ENTITY_ID).build();
        recipe.addIngredient(Ingredient.builder().id(ENTITY_ID).build());
        recipe.addIngredient(Ingredient.builder().id("2").build());

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        //then
        NotFoundException ex =
                assertThrows(NotFoundException.class, () -> ingredientService.findByRecipeIdAndIngredientId(ENTITY_ID
                        , "3").block());

        //when
        assertEquals("Ingredient Not Found for id: 3", ex.getMessage());
        verify(recipeRepository).findById(anyString());
    }


    @Test
    void testUpdateRecipeCommand() {
        //given
        IngredientCommand command = IngredientCommand.builder()
                .id("3")
                .recipeId("2")
                .amount(BigDecimal.ONE)
                .description("description")
                .unitOfMeasure(UnitOfMeasureCommand.builder()
                                       .id(ENTITY_ID)
                                       .description("uom")
                                       .build())
                .build();

        Recipe recipe = Recipe.builder()
                .id("2")
                .build();
        recipe.addIngredient(Ingredient.builder().id(ENTITY_ID).build());
        recipe.addIngredient(Ingredient.builder().id("2").build());
        recipe.addIngredient(Ingredient.builder().id("3").build());
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        Recipe savedRecipe = Recipe.builder().id("2").build();
        savedRecipe.addIngredient(Ingredient.builder().id("3").build());

        UnitOfMeasure uom = UnitOfMeasure.builder().id(ENTITY_ID).description("uom").build();
        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(Mono.just(savedRecipe));
        when(unitOfMeasureRepository.findById(ENTITY_ID)).thenReturn(Mono.just(uom));

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        //then
        assertEquals("3", savedCommand.getId());
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
        verify(unitOfMeasureRepository).findById(ENTITY_ID);

    }


    @Test
    void testSaveNewRecipeCommand() {
        //given
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .recipeId("2")
                .amount(BigDecimal.ONE)
                .description("description")
                .unitOfMeasure(UnitOfMeasureCommand.builder()
                                       .id(ENTITY_ID)
                                       .build())
                .build();
        Recipe recipe = Recipe.builder().id("2").build();
        Mono<Recipe> recipeOptional = Mono.just(recipe);

        Recipe savedRecipe = Recipe.builder().id("2").build();
        Ingredient ingredient = Ingredient.builder()
                .id("3")
                .amount(BigDecimal.ONE)
                .description("description")
                .unitOfMeasure(UnitOfMeasure.builder()
                                       .id(ENTITY_ID)
                                       .description("uom")
                                       .build())
                .build();
        savedRecipe.addIngredient(ingredient);

        when(recipeRepository.findById("2")).thenReturn(recipeOptional);
        when(recipeRepository.save(recipe)).thenReturn(Mono.just(savedRecipe));

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand).block();

        //then
        assertEquals("3", savedCommand.getId());
        assertEquals(ingredientCommand.getAmount(), savedCommand.getAmount());
        assertEquals(ingredientCommand.getDescription(), savedCommand.getDescription());
        assertEquals(ingredientCommand.getUnitOfMeasure().getId(), savedCommand.getUnitOfMeasure().getId());
        verify(recipeRepository).findById("2");
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void deleteRecipeIngredient() {
        //given
        ArgumentCaptor<Recipe> acRecipe = ArgumentCaptor.forClass(Recipe.class);
        Recipe recipe = Recipe.builder().id(ENTITY_ID).build();
        recipe.addIngredient(Ingredient.builder().id(ENTITY_ID).build());
        recipe.addIngredient(Ingredient.builder().id("2").build());
        recipe.addIngredient(Ingredient.builder().id("3").build());
        when(recipeRepository.findById(ENTITY_ID)).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(recipe)).thenReturn(Mono.just(recipe));

        //when
        ingredientService.deleteByRecipeIdAndIngredientId(ENTITY_ID, "2").block();

        //then
        verify(recipeRepository).findById(ENTITY_ID);
        verify(recipeRepository).save(acRecipe.capture());
        Recipe capturedRecipe = acRecipe.getValue();
        assertNotNull(capturedRecipe);
        assertEquals(2, capturedRecipe.getIngredients().size());
        assertTrue(capturedRecipe.getIngredients().contains(Ingredient.builder().id(ENTITY_ID).build()));
        assertFalse(capturedRecipe.getIngredients().contains(Ingredient.builder().id("2").build()));
        assertTrue(capturedRecipe.getIngredients().contains(Ingredient.builder().id("3").build()));
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeRepository,
                                 unitOfMeasureRepository);
    }
}
