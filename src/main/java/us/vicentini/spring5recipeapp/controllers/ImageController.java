package us.vicentini.spring5recipeapp.controllers;

import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    @GetMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable Long recipeId, Model model) {
        RecipeCommand recipe = recipeService.findCommandById(recipeId);

        model.addAttribute("recipe", recipe);

        return "recipe/image-upload-form";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String uploadImage(@PathVariable Long recipeId, @RequestPart("image_file") MultipartFile imageFile) {
        imageService.saveImageFile(recipeId, imageFile);

        return "redirect:/recipe/" + recipeId + "/show";
    }
}
