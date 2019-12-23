package us.vicentini.spring5recipeapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
//Lorg/springframework/boot/test/autoconfigure/jdbc/AutoConfigureTestDatabase;
class Spring5RecipeAppApplicationIT {

    @Test
    void contextLoads() {
    }

}
