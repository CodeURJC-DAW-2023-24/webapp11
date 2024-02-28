package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AjaxService {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    private List<List<Event>> allEvents;
    private List<Integer> nextEventIndex;
    private int eventsRefreshSize = 3;

    // number of possible events ajax
    private int max = 9;
    public AjaxService() {
        this.allEvents = new ArrayList<>();
        this.nextEventIndex = new ArrayList<>();
        int i;
        for (i = 0; i<max; i++){
            this.allEvents.add(new ArrayList<>());
            this.nextEventIndex.add(0);
        }
    }
    public List<Event> findAjax(int i){
        return findAjax(-1L, i);
    }

    public List<Event> findAjax(Long id, int i){
        return findAjax(id, i, "", 0);
    }

    public List<Event> findAjax(Long id, int i, int dest){
        return findAjax(id, i, "", dest);
    }

    public List<Event> findAjax(int i, String input, int dest){
        return findAjax(-1L, i, input, dest);
    }

    public List<Event> findAjax(Long id, int i, String input, int dest){
        int aux = i;
        switch (aux){
            case 0 :
                this.allEvents.set(aux, eventService.findAll());
                break;
            case 1 :
                this.allEvents.set(aux, eventService.findByCreatorIdCurrentCreatedEvents(id));
                break;
            case 2 :
                this.allEvents.set(aux, eventService.findByCreatorIdPastCreatedEvents(id));
                break;
            case 3:
                this.allEvents.set(aux, eventService.findByRegisteredUserIdCurrentEvents(id));
                break;
            case 4:
                this.allEvents.set(aux, eventService.findByRegisteredUserIdPastEvents(id));
                break;
            case 5:
                this.allEvents.set(aux, eventService.eventsOrderedByPopularity());
                break;
            case 6:
                this.allEvents.set(aux, userService.getUserCategoryPreferences(id));
                break;
            case 7:
                this.allEvents.set(dest, eventService.findByCategory(id));
                aux = dest;
                break;
            case 8:
                this.allEvents.set(dest, eventService.findBySearchBar(input));
                aux = dest;
                break;

        }
        this.nextEventIndex.set(aux, this.eventsRefreshSize);
        if (allEvents.get(aux).isEmpty()){
            return new ArrayList<>();
        }else if (allEvents.get(aux).size() <= nextEventIndex.get(aux)){
            return allEvents.get(aux).subList(0,allEvents.get(aux).size());
        }
        return allEvents.get(aux).subList(0,this.eventsRefreshSize);
    }

    public int getNextEventIndex(int i) {
        return nextEventIndex.get(i);
    }

    public int getEventsRefreshSize() {
        return eventsRefreshSize;
    }

    public List<Event> getAllEvents(int i) {
        return allEvents.get(i);
    }


    public void setNextEventIndex(int i, int nextEventIndex) {
        this.nextEventIndex.set(i, nextEventIndex);
    }
}
