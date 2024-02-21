package com.EventCrafters.EventCrafters.controller;

import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.EventCrafters.EventCrafters.service.EventService;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EventWebController {

    @Autowired
    private EventService service;

    private List<Event> allEvents;

    private int nextEventIndex = 3;
    private int eventsRefreshSize = 3;


    @GetMapping("/")
    public String home(Model model) {
        this.allEvents = service.findAll();
        model.addAttribute("events", allEvents.subList(0,nextEventIndex));
        model.addAttribute("nextEventIndex", nextEventIndex);
        model.addAttribute("eventsRefreshSize", eventsRefreshSize);
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


/*
    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("logged", false);
        }
    }

    }
    @GetMapping("/home/search")
    public String search(Model model) {
        //To-do: implement the whole thing.
        return "index";
    }
    @GetMapping("/home/{tag}")
    public String filter(Model model) {
        //To-do: implement the whole thing.
        return "index";
    }

    @GetMapping("/events/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        //To-do: implement the whole thing. Maybe take inspiration from this
        /*Optional<Event> book = service.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "book";
        } else {
            return "books";
        }*/
/*        return "event_info";
    }

    @PostMapping("/removeEvent/{id}")
    public String removeBook(Model model, @PathVariable long id) {
        //To-do: implement the whole thing. Maybe take inspiration from this
        /*
        Optional<Event> book = service.findById(id);
        if (book.isPresent()) {
            service.delete(id);
            model.addAttribute("book", book.get());
        }*/
/*        return "profile"; //or a dedicated page to tell the user the operation went through
    }

    @GetMapping("/newEvent")
    public String newEvent(Model model) {
        //To-do: Give model neccesary params
        return "create_event";
    }

    @PostMapping("/newEvent")
    public String newEventProcess(Model model, Event event) {
        //To-do: save the event. Maybe take inspiration from this
        /* service.save(event);
        model.addAttribute("bookId", event.getId()); */
/*        return "profile"; //or event_info for this event, or a dedicated page to tell the user the operation went through
    }

    @GetMapping("/editEvent/{id}")
    public String editBook(Model model, @PathVariable long id) {
        //To-do: implement the whole thing. Maybe take inspiration from this
        /*
        Optional<Event> book = service.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "editBookPage";
        } else {
            return "books";
        }
        */
/*        return "create_event"; //?
    }

    @PostMapping("/editEvent") //wouldn't we take the id as well?
    public String editBookProcess(Model model, Event event) {
        //To-do: implement the whole thing. Maybe take inspiration from this
        /*service.save(event);
        model.addAttribute("bookId", event.getId());*/

/*        return "event_info"; //or profile for this event, or a dedicated page to tell the user the operation went through
    }
*/
}
