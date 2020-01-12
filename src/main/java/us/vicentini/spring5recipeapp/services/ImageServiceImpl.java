package us.vicentini.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.exceptions.NotFoundException;
import us.vicentini.spring5recipeapp.repositories.RecipeReactiveRepository;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeRepository;


    @Override
    public Mono<Void> saveImageFile(String recipeId, FilePart filePart) {
        return Mono.zip(recipeRepository.findById(recipeId),
                        saveToTmpFile(filePart))
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe Not Found for id: " + recipeId)))
                .flatMap(objects -> {
                    Recipe recipe = objects.getT1();
                    recipe.setImage(getImageBytes(objects.getT2()));
                    return Mono.just(recipe);
                })
                .flatMap(recipeRepository::save)
                .then();
    }


    @SneakyThrows
    private Mono<File> saveToTmpFile(FilePart filePart) {
        File tmpImageFile = File.createTempFile("image-", filePart.filename());
        log.info(tmpImageFile.getAbsolutePath());
        return filePart
                .transferTo(tmpImageFile)
                .thenReturn(tmpImageFile);
    }


    @SneakyThrows
    private Byte[] getImageBytes(File tmpImageFile) {
        byte[] originalFile = Files.readAllBytes(tmpImageFile.toPath());
        Byte[] imageBytes = new Byte[originalFile.length];
        Arrays.setAll(imageBytes, n -> originalFile[n]);
        tmpImageFile.delete();
        return imageBytes;
    }
}
