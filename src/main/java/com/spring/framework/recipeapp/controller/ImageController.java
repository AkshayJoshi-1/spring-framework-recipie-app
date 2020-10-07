package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.service.ImageService;
import com.spring.framework.recipeapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequestMapping("/recipe/{recipeId}")
public class ImageController {

    private final ImageService imageService;

    private final RecipeService recipeService;


    public ImageController(@Autowired ImageService imageService,
                           @Autowired RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("image")
    public String showImageUpload(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
        return "recipe/imageuploadform";
    }

    @PostMapping("image")
    public String handleImagePost(@PathVariable String recipeId,
                                  @RequestParam("imageFile") MultipartFile imageFile) {
        imageService.saveImageFile(Long.valueOf(recipeId), imageFile);
        return "redirect:/recipe" + recipeId + "/show";
    }
}
