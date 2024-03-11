package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.DTO.CategoryDTO;
import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EventService eventService;

    private CategoryDTO transformToDTO(Category category){
        Set<Long> eventsIdInCategories = new HashSet<>();
        String name = category.getName();
        String color = category.getColor();
        Long id = category.getId();
        for (Event e : category.getEventsInCategories()){
            eventsIdInCategories.add(e.getId());
        }
        return new CategoryDTO(id, name, color, eventsIdInCategories);

    }

    private Category transformFromDTO(CategoryDTO category){
        Set<Event> eventsIdInCategories = new HashSet<>();
        String name = category.getName();
        String color = category.getColor();
        Long id = category.getId();
        for (Long e : category.getEventIdInCategories()){
            if (eventService.findById(e).isPresent()) {
                eventsIdInCategories.add(eventService.findById(e).get());
            }

        }
        return new Category(id, name, color, eventsIdInCategories);

    }
    @GetMapping("/categories")
    public List<CategoryDTO> showCategories(){
        List<Category> all = categoryService.findAll();
        List<CategoryDTO> answer = new ArrayList<>();
        for (Category c : all){
            CategoryDTO categoryDTO = transformToDTO(c);
            answer.add(categoryDTO);
        }
        return answer;
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> showCategory(@PathVariable Long id){
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()){
            CategoryDTO categoryDTO = this.transformToDTO(category.get());
            return ResponseEntity.status(200).body(categoryDTO);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/category/new")
    public ResponseEntity<Category> newCategory(@RequestBody CategoryDTO category){
        Category newCategory = transformFromDTO(category);
        categoryService.save(newCategory);
        return ResponseEntity.status(200).body(null);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> substituteCategory(@PathVariable Long id, @RequestBody CategoryDTO category){
        Optional<Category> oldCategory = categoryService.findById(id);
        if (oldCategory.isPresent()){
            category.setId(id);
            Category category1 = transformFromDTO(category);
            categoryService.save(category1);
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }
/*
    @PatchMapping("/category/{id}")
    public ResponseEntity<Category> modifyChef(@PathVariable Long id, @RequestBody Category category){
        Optional<Category> oldCategory = categoryService.findById(id);
        if (oldCategory.isPresent()){
            return ResponseEntity.notFound().build();
        }
        //categoryService.modifyToMatch(id,chef);
        return ResponseEntity.ok(categoryService.findById(id).get());
    }

 */

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id){
        if (id != 1) {
            Optional<Category> category = categoryService.findById(id);
            if (category.isPresent()) {
                categoryService.delete(id);
                return ResponseEntity.ok(category.get());
            }
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}