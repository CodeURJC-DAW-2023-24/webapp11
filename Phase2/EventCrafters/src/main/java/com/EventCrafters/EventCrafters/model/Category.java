package com.EventCrafters.EventCrafters.model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author lucia and Tarek
 */


@Data
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToMany (mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> eventsInCategories = new HashSet<>();
    public Category (){
    }

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Category(Long id, String name, String color, Set<Event> eventsInCategories) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.eventsInCategories = eventsInCategories;
    }
}

