package us.vicentini.spring5recipeapp.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import us.vicentini.spring5recipeapp.services.RecipeService;

import java.net.URI;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class IndexControllerMockMvcBuilderTest {

    @InjectMocks
    private IndexController indexController;

    @Mock
    private RecipeService recipeService;

    private WebTestClient webTestClient;

    @BeforeEach
    void init() {
        webTestClient = WebTestClient.bindToController(indexController).build();
    }


    @Test
    void getIndexPageMockMvc() throws Exception {
        when(recipeService.getRecipes()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri(URI.create("/"))
                .exchange();

//
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"));

        verify(recipeService).getRecipes();
    }


    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeService);
    }
}
