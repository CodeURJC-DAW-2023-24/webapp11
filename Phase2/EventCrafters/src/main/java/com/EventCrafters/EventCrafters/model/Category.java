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
    private String category;

    @Column(nullable = false)
    private String color;

    @OneToMany (mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> eventsInCategories = new HashSet<>();
    public Category (){
    }

    public Category(String category, String color) {
        this.category = category;
        this.color = color;
    }

}

