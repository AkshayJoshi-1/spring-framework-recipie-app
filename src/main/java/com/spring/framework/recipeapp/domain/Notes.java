package com.spring.framework.recipeapp.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@ToString(exclude = {"recipe"})
public class Notes {

    @Id
    private String id;

    private Recipe recipe;

    private String recipeNotes;
}
