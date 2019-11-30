package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.repositories.RecipeRepository;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;


    @Override
    public void saveImageFile(Long recipeId, MultipartFile multipartFile) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    throw new RuntimeException("Recipe Not Found for id: " + recipeId);
                });
        recipe.setImage(getImageBytes(multipartFile));
        recipeRepository.save(recipe);
    }

    private Byte[] getImageBytes(MultipartFile multipartFile) {
        try {
            byte[] originalFile = multipartFile.getBytes();
            Byte[] imageBytes = new Byte[originalFile.length];
            Arrays.setAll(imageBytes, n -> originalFile[n]);
            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("Error getting bytes from image: " + multipartFile.getOriginalFilename(), e);
        }
    }
}
