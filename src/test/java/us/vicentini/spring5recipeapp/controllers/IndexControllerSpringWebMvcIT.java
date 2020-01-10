package us.vicentini.spring5recipeapp.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.services.RecipeService;

import java.net.URI;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebFluxTest(IndexController.class)
class IndexControllerSpringWebMvcIT {

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getIndexPageMockMvc() throws Exception {
        Recipe recipe = Recipe.builder()
                .id("1234")
                .description("description")
                .build();
        when(recipeService.getRecipes()).thenReturn(Flux.just(recipe));

        webTestClient.get()
                .uri(URI.create("/"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .xpath("/html/body/div/div/div/div/div[2]/div/table/tr[1]/td[1]").exists()
                .xpath("/html/body/div/div/div/div/div[2]/div/table/tr[1]/td[1]").isEqualTo(recipe.getId())
                .xpath("/html/body/div/div/div/div/div[2]/div/table/tr[1]/td[2]").exists()
                .xpath("/html/body/div/div/div/div/div[2]/div/table/tr[1]/td[2]").isEqualTo(recipe.getDescription());

        verify(recipeService).getRecipes();
    }


    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeService);
    }
}
