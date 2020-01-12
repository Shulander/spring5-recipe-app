package us.vicentini.spring5recipeapp.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import us.vicentini.spring5recipeapp.config.RouterFunctionsConfig;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.services.RecipeService;

import static org.mockito.Mockito.when;

public class RouterFunctionTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RouterFunctionsConfig routerFunctionsConfig = new RouterFunctionsConfig();
        RouterFunction<ServerResponse> routerFunction = routerFunctionsConfig.routes(recipeService);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }


    @Test
    public void testGetRecipes() {
        when(recipeService.getRecipes()).thenReturn(Flux.just());

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    public void testGetRecipesWithData() {
        when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Recipe.class);
    }
}
