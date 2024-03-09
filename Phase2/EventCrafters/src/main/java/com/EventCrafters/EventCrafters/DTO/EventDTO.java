package com.EventCrafters.EventCrafters.DTO;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EventDTO {
    private Long id;
    private String name;
    private int attendeesCount = -1;
    //private Blob photo;
    private String description;
    private int maxCapacity;
    private double price;
    private String location;
    private String map;
    private Date startDate;
    private Date endDate;
    private String additionalInfo;
    private Long creatorId;
    private Set<Long> registeredUsersId = new HashSet<>();
    private int numRegisteredUsers;
    private Set<Long> reviewsId = new HashSet<>();
    private Long categoryId;

    public EventDTO(Long id, String name, int attendeesCount, String description, int maxCapacity, double price, String location, String map, Date startDate, Date endDate, String additionalInfo, Long creatorId, Set<Long> registeredUsersId, int numRegisteredUsers, Set<Long> reviewsId, Long categoryId) {
        this.id = id;
        this.name = name;
        this.attendeesCount = attendeesCount;
        this.description = description;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.location = location;
        this.map = map;
        this.startDate = startDate;
        this.endDate = endDate;
        this.additionalInfo = additionalInfo;
        this.creatorId = creatorId;
        this.registeredUsersId = registeredUsersId;
        this.numRegisteredUsers = numRegisteredUsers;
        this.reviewsId = reviewsId;
        this.categoryId = categoryId;
    }

    public EventDTO() {
    }
}
