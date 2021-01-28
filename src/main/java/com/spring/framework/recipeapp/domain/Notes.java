package com.spring.framework.recipeapp.domain;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@ToString(exclude = {"recipe"})
public class Notes {

    private String id;

    private Recipe recipe;

    private String recipeNotes;
}
