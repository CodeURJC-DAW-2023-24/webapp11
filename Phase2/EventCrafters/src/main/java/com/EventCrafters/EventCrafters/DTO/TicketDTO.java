package com.EventCrafters.EventCrafters.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {

    private String eventName;

    private String userName;
    private String userNickname;
    private String userMail;

    private String creatorName;
    private String creatorNickname;
    private String creatorMail;

    private int maxCapacity;
    private String location;
    private String startDate;
    private String endDate;
    private String duration;
    private String additionalInfo;
    private String price;

    public TicketDTO(String eventName, String userName, String userNickname, String userMail, String creatorName, String creatorNickname, String creatorMail, int maxCapacity, String location, String startDate, String endDate, String duration, String additionalInfo, String price) {
        this.eventName = eventName;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userMail = userMail;
        this.creatorName = creatorName;
        this.creatorNickname = creatorNickname;
        this.creatorMail = creatorMail;
        this.maxCapacity = maxCapacity;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.additionalInfo = additionalInfo;
        this.price = price;
    }

}
