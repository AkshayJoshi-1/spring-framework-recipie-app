package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.repository.reactive.RecipeReactiveRepository;
import com.spring.framework.recipeapp.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeRepository;

    public ImageServiceImpl(@Autowired RecipeReactiveRepository recipeService) {
        this.recipeRepository = recipeService;
    }

    @Override
    public Mono<Recipe> saveImageFile(String recipeId, MultipartFile file) {

        return recipeRepository.save(Objects.requireNonNull(recipeRepository.findById(recipeId)
                .map(recipe -> {
                    fillImageBytes(file, recipe);
                    return recipe;
                }).block()));
    }

    private void fillImageBytes(MultipartFile file, Recipe recipe) {

        try {
            Byte[] imageBytes = new Byte[file.getBytes().length];
            int i = 0;
            for (byte imageByte : file.getBytes()) {
                imageBytes[i] = imageByte;
                i++;
            }

            recipe.setImage(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
