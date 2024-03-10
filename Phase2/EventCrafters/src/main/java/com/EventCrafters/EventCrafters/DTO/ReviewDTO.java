package com.EventCrafters.EventCrafters.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private Long userId;
    private Long eventId;
    private int rating;
    private String text;

    public ReviewDTO(Long id, Long userId, Long eventId, int rating, String text) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.rating = rating;
        this.text = text;
    }
}
