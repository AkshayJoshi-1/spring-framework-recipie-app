package com.spring.framework.recipeapp.controller;

import com.spring.framework.recipeapp.command.RecipeCommand;
import com.spring.framework.recipeapp.service.ImageService;
import com.spring.framework.recipeapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        return "recipe/imageuploadform";
    }

    @PostMapping("image")
    public String handleImagePost(@PathVariable String recipeId,
                                  @RequestParam("imageFile") MultipartFile imageFile) {
        imageService.saveImageFile(recipeId, imageFile);
        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("getImage")
    public void renderImage(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);

        if(recipeCommand == null || recipeCommand.getImage() == null) {
            return;
        }

        byte[] imageBytes = new byte[recipeCommand.getImage().length];

        for(int i = 0; i < imageBytes.length; i++) {
            imageBytes[i] = recipeCommand.getImage()[i];
        }

        response.setContentType("image/jepg");
        InputStream imageStream = new ByteArrayInputStream(imageBytes);

        IOUtils.copy(imageStream, response.getOutputStream());
    }
}
