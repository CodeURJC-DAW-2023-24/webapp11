package com.EventCrafters.EventCrafters.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class EventFinishedDTO extends EventDTO {
    private int attendeesCount;
    private int reviewNum;
    private double averageRating;

    public EventFinishedDTO() {

    }

    public EventFinishedDTO(Long id, String name, String description, int maxCapacity, double price, String location, String map, Date startDate, Date endDate, String additionalInfo, Long creatorId, int numRegisteredUsers, Long categoryId, int attendeesCount, double averageRating, int reviewNum) {
        super(id, name, description, maxCapacity, price, location, map, startDate, endDate, additionalInfo, creatorId, numRegisteredUsers, categoryId);
        this.attendeesCount = attendeesCount;
        this.averageRating = averageRating;
        this.reviewNum = reviewNum;
    }

}
