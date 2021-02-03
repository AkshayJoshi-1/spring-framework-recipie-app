package com.spring.framework.recipeapp.service;

import com.spring.framework.recipeapp.domain.Recipe;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ImageService {

    Mono<Recipe> saveImageFile(String recipeId, MultipartFile file);
}
