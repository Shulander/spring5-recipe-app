package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.services.ImageService;
import us.vicentini.spring5recipeapp.services.RecipeService;

import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;


    @GetMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable String recipeId, Model model) {
        Mono<RecipeCommand> recipe = recipeService.findCommandById(recipeId);

        model.addAttribute("recipe", recipe);

        return "recipe/image-upload-form";
    }


    @PostMapping("/recipe/{recipeId}/image")
    public Mono<String> uploadImage(@PathVariable String recipeId,
                                    @RequestPart("image_file") Mono<FilePart> imageFile) {
        return imageFile
                .flatMap(filePart -> imageService.saveImageFile(recipeId, filePart))
                .thenReturn("redirect:/recipe/" + recipeId + "/show");
    }


    @ResponseBody
    @GetMapping(value = "/recipe/{recipeId}/recipeimage", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getImage(@PathVariable String recipeId) {
        return recipeService.findCommandById(recipeId)
                .switchIfEmpty(Mono.just(RecipeCommand.builder().build()))
                .map(recipeCommand -> recipeCommand.getImage() == null ? new Byte[0] : recipeCommand.getImage())
                .map(this::getBytes);
    }


    private byte[] getBytes(Byte[] originalFile) {
        if (originalFile == null || originalFile.length == 0) {
            return getDefaultFoodImage();
        }
        byte[] byteImage = new byte[originalFile.length];
        for (int i = 0; i < byteImage.length; i++) {
            byteImage[i] = originalFile[i];
        }
        return byteImage;
    }


    @SneakyThrows
    private byte[] getDefaultFoodImage() {
        Path path = ResourceUtils
                .getFile("classpath:static/images/default-food-image.jpg")
                .toPath();
        return Files.readAllBytes(path);
    }
}
