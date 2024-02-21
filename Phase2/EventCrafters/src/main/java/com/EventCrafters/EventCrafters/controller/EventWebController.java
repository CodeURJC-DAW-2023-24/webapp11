package com.EventCrafters.EventCrafters.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.EventCrafters.EventCrafters.service.EventService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EventWebController {

    @Autowired
    private EventService service;

    private List<Event> allEvents;

    private int nextEventIndex = 3;
    private int eventsRefreshSize = 3;


    @GetMapping("/")
    public String home(Model model) {
        nextEventIndex = eventsRefreshSize;
        this.allEvents = service.findAll();

        if (allEvents.isEmpty()){
            model.addAttribute("events", new ArrayList<Event>());
            nextEventIndex = allEvents.size();
        }
        else if (allEvents.size() <= nextEventIndex){
            model.addAttribute("events", allEvents.subList(0,allEvents.size()));
            nextEventIndex = allEvents.size();
        }
        else{
            model.addAttribute("events", allEvents.subList(0,nextEventIndex));
        }
        return "index";
    }

    @GetMapping("/newEvents")
    public String newEvents(Model model) {
        int remainingEvents = allEvents.size() - nextEventIndex;

        if (remainingEvents > 0) {
            int endIndex = nextEventIndex + Math.min(eventsRefreshSize, remainingEvents);
            model.addAttribute("additionalEvents", allEvents.subList(nextEventIndex, endIndex));
            nextEventIndex = endIndex;
        }

        return "moreEvents";
    }

    @GetMapping("/create_event")
    public String createEvent(){
        return "create_event";
    }

    @GetMapping("/event/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        Optional<Event> event = service.findById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            return "eventInfo";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/create_event")
    public String createEvent(@RequestParam("name") String name,
                              @RequestParam("photo") MultipartFile photo,
                              @RequestParam("description") String description,
                              @RequestParam("capacity") int maxCapacity,
                              @RequestParam("price") double price,
                              @RequestParam("location") String location,
                              @RequestParam("coordinates") String coordinates,
                              @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                              @RequestParam(value = "additionalInfo", required = false) String additionalInfo,
                              RedirectAttributes redirectAttributes) {
        try {
            Blob photoBlob = new javax.sql.rowset.serial.SerialBlob(photo.getBytes());
            String[] latLong = coordinates.split(",");
            Double latitude = Double.parseDouble(latLong[0].trim());
            Double longitude = Double.parseDouble(latLong[1].trim());
            Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

            Event event = new Event(name, photoBlob, description, maxCapacity, price, location, latitude, longitude, start, end, additionalInfo);
            service.save(event);
        } catch (Exception e) {
        }

        return "redirect:/";
    }
    @GetMapping("/event/image/{id}")
    @ResponseBody
    public byte[] showEventImage(@PathVariable long id) throws SQLException, IOException {
        Optional<Event> eventOptional = service.findById(id);
        if (eventOptional.isPresent()) {
            Blob photoBlob = eventOptional.get().getPhoto();
            int blobLength = (int) photoBlob.length();
            byte[] blobAsBytes = photoBlob.getBytes(1, blobLength);
            photoBlob.free();
            return blobAsBytes;
        } else {
            return new byte[0];
        }
    }
}
