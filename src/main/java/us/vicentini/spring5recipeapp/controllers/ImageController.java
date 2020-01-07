package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import us.vicentini.spring5recipeapp.commands.RecipeCommand;
import us.vicentini.spring5recipeapp.services.ImageService;
import us.vicentini.spring5recipeapp.services.RecipeService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    @GetMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable String recipeId, Model model) {
        RecipeCommand recipe = recipeService.findCommandById(recipeId).block();

        model.addAttribute("recipe", recipe);

        return "recipe/image-upload-form";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String uploadImage(@PathVariable String recipeId, @RequestPart("image_file") MultipartFile imageFile) {
        imageService.saveImageFile(recipeId, imageFile).block();

        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("/recipe/{recipeId}/recipeimage")
    public void getImage(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipe = recipeService.findCommandById(recipeId).block();
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        Byte[] originalFile = recipe.getImage();
        byte[] byteImage = getBytes(originalFile);
        InputStream is = new ByteArrayInputStream(byteImage);
        IOUtils.copy(is, response.getOutputStream());
    }

    private byte[] getBytes(Byte[] originalFile) {
        byte[] byteImage = new byte[originalFile.length];
        for (int i = 0; i < byteImage.length; i++) {
            byteImage[i] = originalFile[i];
        }
        return byteImage;
    }
}
