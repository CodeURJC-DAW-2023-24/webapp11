package com.EventCrafters.EventCrafters.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(name = "EventManipulationDTO", description = "For event creation and edition.")
public class EventManipulationDTO {
    private String name;
    private String description;
    private int maxCapacity;
    private double price;
    private String location;
    private String map;
    private Date startDate;
    private Date endDate;
    private String additionalInfo;
    private Long categoryId;

    public EventManipulationDTO() {
    }

    public EventManipulationDTO(String name, String description, int maxCapacity, double price, String location, String map, Date startDate, Date endDate, String additionalInfo, Long categoryId) {
        this.name = name;
        this.description = description;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.location = location;
        this.map = map;
        this.startDate = startDate;
        this.endDate = endDate;
        this.additionalInfo = additionalInfo;
        this.categoryId = categoryId;
    }
}
