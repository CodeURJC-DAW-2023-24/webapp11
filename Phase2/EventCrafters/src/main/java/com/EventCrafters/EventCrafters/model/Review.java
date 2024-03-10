package com.EventCrafters.EventCrafters.model;

import javax.persistence.*;
import lombok.Data;
/**
 *

 @author Lucia and Tarek*/


@Data
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="event_id")
    private Event event;

    private int rating;

    private String text;

    public Review() {}

    public Review(int rating, String textValoration) {
        this.rating = rating;
        this.text = textValoration;
    }

    public Review(Long id, User user, Event event, int rating, String text) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.rating = rating;
        this.text = text;
    }
}