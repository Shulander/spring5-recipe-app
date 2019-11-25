package us.vicentini.spring5recipeapp.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.services.RecipeService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {


    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController controller;


    @Test
    void testGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testNewRecipe() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

    }

    @Test
    void testPersistNewRecipe() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Long recipeID = 3L;
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class)))
                .thenReturn(RecipeCommand.builder().id(recipeID).build());

        mockMvc.perform(post("/recipe/")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "")
                                .param("description", "my+description")
                                .param("prepTime", "1")
                                .param("cookTime", "2")
                                .param("servings", "3")
                                .param("source", "source")
                                .param("url", "url.com")
                                .param("directions", "directions")
                                .param("notes.recipeNotes", "notes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/3/show/"));
    }

    @Test
    void testUpdateRecipeView() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(recipeService.findCommandById(1L)).thenReturn(RecipeCommand.builder().id(1L).build());

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }
}
