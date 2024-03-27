package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.DTO.CategoryDTO;
import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
        Category newCategory = new Category();

        newCategory.setName(category.getName());
        newCategory.setColor(category.getColor());

        // solo se cambian las categorias de los eventos sin categoría
        for (Long e : category.getEventIdInCategories()){
            if (eventService.findById(e).isPresent()) {
                Event event = eventService.findById(e).get();
                if (event.getCategory().getId() == 1){
                    event.setCategory(newCategory);
                }
            }

        }
        return newCategory;

    }
    @GetMapping("/categories")
    @Operation(summary = "Retrieves the categories available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories obtained",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
    })
    public List<CategoryDTO> showCategories(@RequestParam("page") int page){
        List<Category> all;
        if (page != -1){
            all = categoryService.findAll(page);
        } else {
            all = categoryService.findAll();
        }

        List<CategoryDTO> answer = new ArrayList<>();
        for (Category c : all){
            CategoryDTO categoryDTO = transformToDTO(c);
            answer.add(categoryDTO);
        }
        return answer;
    }
    @GetMapping("/categories/{id}")
    @Operation(summary = "Retrieves the category, with the ID specified in the url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category obtained",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
    })
    public ResponseEntity<CategoryDTO> showCategory(@PathVariable Long id){
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()){
            CategoryDTO categoryDTO = this.transformToDTO(category.get());
            return ResponseEntity.status(200).body(categoryDTO);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/categories")
    @Operation(summary = "Adds a category to the database and returns the url to visualize it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
    })
    public ResponseEntity<String> newCategory(@RequestBody CategoryDTO category){
        category.setId(-1L);
        Category newCategory = transformFromDTO(category);
        categoryService.save(newCategory);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCategory.getId()).toUri();
        return ResponseEntity.created(location).body(location.toString());
    }


    @PutMapping("/categories/{id}")
    @Operation(summary = "Modifies an existing category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Not found", content=@Content),
    })
    public ResponseEntity<String> substituteCategory(@PathVariable Long id, @RequestBody CategoryDTO category){
        if  (id != 1){  // the first category is the default one, it can´t be modified
            Optional<Category> oldCategory = categoryService.findById(id);
            if (oldCategory.isPresent()){
                Set<Event> events = oldCategory.get().getEventsInCategories();
                for (Event e : events ){
                    e.setCategory(categoryService.findById(1).get());
                }
                category.setId(id);
                Category category1 = transformFromDTO(category);
                categoryService.save(category1);
                URI location = ServletUriComponentsBuilder.fromHttpUrl("https://localhost:8443").path("/api/categories/{id}")
                        .buildAndExpand(id).toUri();
                return ResponseEntity.created(location).body(location.toString());
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Deletes a specific category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Operation not permitted", content=@Content),
            @ApiResponse(responseCode = "404", description = "Not found", content=@Content),
    })
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id){
        if (id != 1) { // the first category is the default one, it can´t be deleted
            Optional<Category> category = categoryService.findById(id);
            if (category.isPresent()) {
                categoryService.delete(id);
                return ResponseEntity.ok(transformToDTO(category.get()));
            }
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
