package com.EventCrafters.EventCrafters.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private String name;
    private String color;

    private Set<Long> eventIdInCategories;

    public CategoryDTO(Long id, String name, String color, Set<Long> eventIdInCategories) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.eventIdInCategories = eventIdInCategories;
    }
}
