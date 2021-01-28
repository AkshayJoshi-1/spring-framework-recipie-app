package com.spring.framework.recipeapp.service.impl;

import com.spring.framework.recipeapp.domain.Recipe;
import com.spring.framework.recipeapp.repository.RecipeRepository;
import com.spring.framework.recipeapp.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(@Autowired RecipeRepository recipeService) {
        this.recipeRepository = recipeService;
    }

    @Override
    public void saveImageFile(String recipeId, MultipartFile file) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isEmpty()) {
            return;
        }

        Recipe recipe = recipeOptional.get();

        try {
            Byte[] imageBytes = new Byte[file.getBytes().length];

            int i = 0;
            for(byte imageByte : file.getBytes()) {
                imageBytes[i] = imageByte;
                i++;
            }

            recipe.setImage(imageBytes);
            recipeRepository.save(recipe);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
