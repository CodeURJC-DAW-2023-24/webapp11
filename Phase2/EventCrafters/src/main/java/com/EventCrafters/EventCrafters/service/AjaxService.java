package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AjaxService {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    private final int pageSize = 3;
    private List<Integer> maxPageNum;

    private List<Long> ids;

    private String input;

    // number of possible events ajax at the same time
    // if the number is changed go to getPageNum and add til that number in the not default case
    // and be careful with the findAjax function
    private int maxEventAjaxSameTime = 4;
    public AjaxService() {
        this.maxPageNum = new ArrayList<>();
        this.ids = new ArrayList<>();
        int i;
        for (i = 0; i<maxEventAjaxSameTime; i++){
            this.maxPageNum.add(0);
            this.ids.add(0L);
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
        int aux;
        List<Event> result = new ArrayList<>();

        switch (i){
            case 0 :
                aux = 0;
                this.maxPageNum.set(aux, (eventService.findAll().size() + pageSize - 1) /pageSize);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.findAll(0, pageSize);
            case 1 :
                aux = 0;
                this.maxPageNum.set(aux, (eventService.findByCreatorIdCurrentCreatedEvents(id).size() + pageSize - 1) /pageSize);
                this.ids.set(aux, id);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.findByCreatorIdCurrentCreatedEvents(id, 0, pageSize);
            case 2 :
                aux = 1;
                this.maxPageNum.set(aux, (eventService.findByCreatorIdPastCreatedEvents(id).size() + pageSize - 1) /pageSize);
                this.ids.set(aux, id);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.findByCreatorIdPastCreatedEvents(id, 0, pageSize);
            case 3:
                aux = 2;
                this.maxPageNum.set(aux, (eventService.findByRegisteredUserIdCurrentEvents(id).size() + pageSize - 1) /pageSize);
                this.ids.set(aux, id);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.findByRegisteredUserIdCurrentEvents(id, 0, pageSize);
                //return new ArrayList<>();
            case 4:
                aux = 3;
                this.maxPageNum.set(aux, (eventService.findByRegisteredUserIdPastEvents(id).size() + pageSize - 1) /pageSize);
                this.ids.set(aux, id);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.findByRegisteredUserIdPastEvents(id, 0, pageSize);
                //return new ArrayList<>();
            case 5:
                aux = 0;
                this.maxPageNum.set(aux, (eventService.eventsOrderedByPopularity().size() + pageSize - 1) /pageSize);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.eventsOrderedByPopularity(0, pageSize);
            case 6:
                aux = 0;
                this.maxPageNum.set(aux, (userService.getUserCategoryPreferencesNum(id) + pageSize - 1) /pageSize);
                this.ids.set(aux, id);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return userService.getUserCategoryPreferences(id, 0, pageSize);
            case 7: // Using dest to save space as it can´t be both at the same time
                aux = 0;
                this.maxPageNum.set(aux, (eventService.findByCategory(id).size() + pageSize - 1) /pageSize);
                this.ids.set(aux, id);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.findByCategory(id, 0, pageSize);
            case 8:
                aux = 0;
                this.maxPageNum.set(aux, (eventService.findBySearchBar(input).size() + pageSize - 1) /pageSize);
                this.input= input;
                this.ids.set(aux, id);
                if(maxPageNum.get(aux) == 0){
                    return result;
                }
                return eventService.findBySearchBar(input, 0, pageSize);

        }
        return result;
    }

    public List<Event> loadMore(int i, int page){
        List<Event> result = new ArrayList<>();
        switch (i){
            case 0 :
                return eventService.findAll(page, pageSize);
            case 1 :
                return eventService.findByCreatorIdCurrentCreatedEvents(ids.get(0), page, pageSize);
            case 2 :
                return eventService.findByCreatorIdPastCreatedEvents(ids.get(1), page, pageSize);
            case 3:
                return eventService.findByRegisteredUserIdCurrentEvents(ids.get(2), page, pageSize);
            case 4:
                return eventService.findByRegisteredUserIdPastEvents(ids.get(3), page, pageSize);
            case 5:
                return eventService.eventsOrderedByPopularity(page, pageSize);
            case 6:
                return userService.getUserCategoryPreferences(ids.get(0), page, pageSize);
            case 7: // Using dest to save space as it can´t be both at the same time
                return eventService.findByCategory(ids.get(0), page, pageSize);
            case 8:
                return eventService.findBySearchBar(input, page, pageSize);

        }
        return result;
    }

    public Integer getMaxPageNum(int i) {
        return switch (i) {
            case 1, 2, 3, 4 -> maxPageNum.get(i - 1);
            default -> maxPageNum.get(0);
        };

    }
}
