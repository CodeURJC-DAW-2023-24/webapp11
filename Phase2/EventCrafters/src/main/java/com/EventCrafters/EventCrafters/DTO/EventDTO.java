package com.EventCrafters.EventCrafters.DTO;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Duration;
import java.util.Date;

@Getter
@Setter
@Schema(name = "EventDTO", description = "Standard response for events.")
public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private int maxCapacity;
    private double price;
    private String location;
    private String map;
    private Date startDate;
    private Date endDate;
    private String duration;
    private String additionalInfo;
    private Long creatorId;
    private int numRegisteredUsers;
    private Long categoryId;
    private String imageUrl;


    public EventDTO() {
    }

    public EventDTO(Long id, String name, String description, int maxCapacity, double price, String location, String map, Date startDate, Date endDate, String additionalInfo, Long creatorId, int numRegisteredUsers, Long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.location = location;
        this.map = map;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration  = formatDuration(startDate, endDate);
        this.additionalInfo = additionalInfo;
        this.creatorId = creatorId;
        this.numRegisteredUsers = numRegisteredUsers;
        this.categoryId = categoryId;
        this.imageUrl = this.generateImageUrl(id);
    }

    private String generateImageUrl(Long id) {
        return "https://localhost:8443/api/events/image/" + id;
    }

    private String formatDuration(Date start, Date end) {
        Duration duration = Duration.between(start.toInstant(), end.toInstant());
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%d horas y %d minutos", hours, minutes);
    }
}
