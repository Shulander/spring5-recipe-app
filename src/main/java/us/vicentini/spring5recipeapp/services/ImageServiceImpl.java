package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.RecipeReactiveRepository;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeRepository;


    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile multipartFile) {
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe Not Found for id: " + recipeId)))
                .map(recipe -> {
                    recipe.setImage(getImageBytes(multipartFile));
                    return recipe;
                })
                .flatMap(recipeRepository::save)
                .flatMap(recipe -> Mono.empty());
    }


    private Byte[] getImageBytes(MultipartFile multipartFile) {
        try {
            byte[] originalFile = multipartFile.getBytes();
            Byte[] imageBytes = new Byte[originalFile.length];
            Arrays.setAll(imageBytes, n -> originalFile[n]);
            return imageBytes;
        } catch (IOException e) {
            throw new NotFoundException("Error getting bytes from image: " + multipartFile.getOriginalFilename(), e);
        }
    }
}
