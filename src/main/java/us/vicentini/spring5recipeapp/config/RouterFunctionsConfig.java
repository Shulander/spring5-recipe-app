package us.vicentini.spring5recipeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.services.RecipeService;

@Configuration
public class RouterFunctionsConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(RecipeService recipeService) {
        return RouterFunctions.route(RequestPredicates.GET("/api/recipes"),
                                     serverRequest -> ServerResponse
                                             .ok()
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .body(recipeService.getRecipes(), Recipe.class));
    }
}
