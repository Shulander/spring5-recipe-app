package us.vicentini.spring5recipeapp.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.repository.RecipeRepository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    public Set<Recipe> getRecipes() {
        return StreamSupport.stream(recipeRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());
    }
}
