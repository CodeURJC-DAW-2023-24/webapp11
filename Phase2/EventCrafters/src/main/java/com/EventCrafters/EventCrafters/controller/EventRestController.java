package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.DTO.EventDTO;
import com.EventCrafters.EventCrafters.DTO.EventFinishedDTO;
import com.EventCrafters.EventCrafters.DTO.EventManipulationDTO;
import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.EventService;
import com.EventCrafters.EventCrafters.service.ReviewService;
import com.EventCrafters.EventCrafters.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.sql.Blob;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    @Operation(summary = "Gets an event by its ID",
            description = "Returns basic event details. Once the event has finished, additional information is included in the response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {EventDTO.class, EventFinishedDTO.class})) }),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    public ResponseEntity<EventDTO> showEvent(@PathVariable long id){
        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isPresent()){
            Event event = eventOptional.get();
            EventDTO eventDTO = transformDTO(event);
            return ResponseEntity.ok(eventDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Creates a new event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventManipulationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid event data", content = @Content),
            @ApiResponse(responseCode = "403", description = "The operation is not allowed without registration", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventManipulationDTO eventManipulationDTO) {
        // Check for empty fields in the event
        if (eventHasEmptyFields(eventManipulationDTO)) {
            return ResponseEntity.badRequest().build();
        }

        // Check if category exists
        Optional<Category> categoryOpt = categoryService.findById(eventManipulationDTO.getCategoryId());
        if (!categoryOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        Event event = transformEvent(eventManipulationDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userOpt = userService.findByUserName(currentUsername);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        event.setCreator(userOpt.get());

        // Set the category
        event.setCategory(categoryOpt.get());

        // Save the event
        Event savedEvent = eventService.save(event);

        // Transform the saved event to EventDTO
        EventDTO eventDTO = transformDTO(savedEvent);

        // Build the URL created event
        URI location = ServletUriComponentsBuilder.fromHttpUrl("https://localhost:8443")
                .path("/api/events/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).body(eventDTO);
    }

    @PutMapping("/{eventId}/photo")
    @Operation(summary = "Uploads a photo for an existing event", description = "Allows uploading a photo for an existing event. Only the event creator or an admin can perform this action.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded",
                    content = { @Content(mediaType = "image/jpeg") }),
            @ApiResponse(responseCode = "403", description = "Current user not is not the creator or an admin"),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error retrieving the image", content = @Content)
    })
    public ResponseEntity<EventDTO> uploadEventPhoto(@PathVariable Long eventId, @RequestPart("photo") MultipartFile photo) {
        Optional<Event> eventOptional = eventService.findById(eventId);
        if (!eventOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOptional.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!isUserAdminOrCreator(currentUsername, event)) {
            // Unauthorized to update the event
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            event.setPhoto(new javax.sql.rowset.serial.SerialBlob(photo.getBytes()));
            eventService.save(event);
            EventDTO eventDTO = transformDTO(event);

            // Build the URL created event
            URI location = ServletUriComponentsBuilder.fromHttpUrl("https://localhost:8443")
                    .path("/api/events/{id}")
                    .buildAndExpand(event.getId())
                    .toUri();

            return ResponseEntity.created(location).body(eventDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/image/{id}")
    @Operation(summary = "Gets the image of an event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found",
                    content = { @Content(mediaType = "image/jpeg") }),
            @ApiResponse(responseCode = "404", description = "Event or image not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error retrieving the image", content = @Content)
    })
    public ResponseEntity<byte[]> showEventImage(@PathVariable long id) {
        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            try {
                Blob photoBlob = event.getPhoto();
                byte[] photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());

                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(photoBytes);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{eventId}/attendees")
    @Operation(summary = "sets the number of attendees for an event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Number of attendees updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventFinishedDTO.class))),
            @ApiResponse(responseCode = "400", description = "Provided information not valid", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation not permitted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    public ResponseEntity<EventDTO> updateEventAttendees(@PathVariable Long eventId, @RequestBody Integer attendeesCount) {


        Optional<Event> eventOpt = eventService.findById(eventId);
        if (!eventOpt.isPresent()) {
            // Event not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Event event = eventOpt.get();
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!isUserAdminOrCreator(currentUsername, event)) {
            // Unauthorized to update the event
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (event.getEndDate().before(new Date()) && attendeesCount != null && attendeesCount >= 0 && attendeesCount <= event.getNumRegisteredUsers() && event.getAttendeesCount() == -1) {
            // Update the attendees count (the event has already ended)
            event.setAttendeesCount(attendeesCount);
            eventService.save(event);

            EventDTO eventDTO = transformDTO(event);
            // Return the "Location" header pointing to the event's URL
            String eventUrl = "https://localhost:8443/api/events/" + eventId;
            return ResponseEntity.ok().header("Location", eventUrl).body(eventDTO);
        } else {
            // Event has not ended yet
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/graph/{eventId}")
    @Operation(summary = "Gets graph data of the event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Graph data obtained",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", description = "Operation not permitted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    public ResponseEntity<Map<String, Integer>> getEventGraphData(@PathVariable Long eventId) {

        Optional<Event> eventOptional = eventService.findById(eventId);
        if(!eventOptional.isPresent()) {
            return ResponseEntity.notFound().build();

        }
        Event event = eventOptional.get();
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!isUserAdminOrCreator(currentUsername, event)) {
            // Unauthorized to update the event
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Map<String, Integer> graphData = new HashMap<>();
        if (event.getAttendeesCount() == -1) {
            // If the attendees count has not been set returning -1 for both registeredUsers and didNotAttend to indicate data is not available
            graphData.put("registeredUsers", -1);
            graphData.put("attendeesCount", -1);
            graphData.put("didNotAttend", -1);
        } else {
            // If attendees count is available, populate the map with actual data
            graphData.put("registeredUsers", event.getNumRegisteredUsers());
            graphData.put("attendeesCount", event.getAttendeesCount());
            int didNotAttend = event.getNumRegisteredUsers() - event.getAttendeesCount();
            graphData.put("didNotAttend", didNotAttend);
        }

        return ResponseEntity.ok(graphData);
    }

    @GetMapping("/AdminProfile/graph")
    @Operation(summary = "Gets graph data of categories in relation to events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Graph data obtained",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", description = "Operation not permitted", content = @Content),
    })
    public ResponseEntity<Map<String, Integer>> getAdminProfileGraphData() {

        Map<String, Integer> graphData = new HashMap<>();
        List<String> labels = categoryService.findAllNames();
        List<Integer> data = categoryService.categoriesNumbers();
        for (int i= 0; i<labels.size(); i++){
            graphData.put(labels.get(i), data.get(i));
        }

        return ResponseEntity.ok(graphData);
    }

    @GetMapping("/recommended")
    public List<EventDTO> recommendedEvents(@RequestParam("page") int page){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        int pageSize = 3;
        List<Event> events = new ArrayList<>();
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (authentication.getName().equals("anonymousUser")) {
            events = eventService.eventsOrderedByPopularity(page, pageSize);
            System.out.println(events.size());
        } else {
            String currentUsername = authentication.getName();
            Optional<User> userOp = userService.findByUserName(currentUsername);
            if (userOp.isPresent()){
                User user = userOp.get();
                events = userService.getUserCategoryPreferences(user.getId(), page, pageSize);
            }
        }

        for (Event e : events){
            eventDTOS.add(transformDTO(e));
        }
        return eventDTOS;

    }

    @GetMapping("/filter/category/{id}")
    public List<EventDTO> filterByCategory(@RequestParam("page") int page, @PathVariable long id){
        int pageSize = 3;
        List<Event> events = eventService.findByCategory(id,page, pageSize);
        List<EventDTO> eventDTOS = new ArrayList<>();
        for (Event e: events){
            eventDTOS.add(transformDTO(e));
        }
        return eventDTOS;
    }

    @GetMapping("/filter/searchBar")
    public List<EventDTO> filterBySearchBar(@RequestParam("page") int page, @RequestParam("input") String input){
        int pageSize = 3;
        List<Event> events = eventService.findBySearchBar(input, page, pageSize);
        List<EventDTO> eventDTOS = new ArrayList<>();
        for (Event e: events){
            eventDTOS.add(transformDTO(e));
        }
        return eventDTOS;
    }

    @GetMapping("/user/created/present")
    public ResponseEntity<List<EventDTO>> userPresentCreatedEvents(@RequestParam("page") int page, Principal principal){
        int pageSize = 3;
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (principal == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUserName(principal.getName());
        if (userOp.isPresent()){
            List<Event> events = eventService.findByCreatorIdCurrentCreatedEvents(userOp.get().getId(), page, pageSize);
            for (Event e : events){
                eventDTOS.add(transformDTO(e));
            }
        }

        return ResponseEntity.ok(eventDTOS);
    }

    @GetMapping("/user/created/past")
    public ResponseEntity<List<EventDTO>> userPastCreatedEvents(@RequestParam("page") int page, Principal principal){
        int pageSize = 3;
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (principal == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUserName(principal.getName());
        if (userOp.isPresent()){
            List<Event> events = eventService.findByCreatorIdPastCreatedEvents(userOp.get().getId(), page, pageSize);
            for (Event e : events){
                eventDTOS.add(transformDTO(e));
            }
        }

        return ResponseEntity.ok(eventDTOS);
    }

    @GetMapping("/user/registered/present")
    public ResponseEntity<List<EventDTO>> userPresentRegisteredEvents(@RequestParam("page") int page, Principal principal){
        int pageSize = 3;
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (principal == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUserName(principal.getName());
        if (userOp.isPresent()){
            List<Event> events = eventService.findByRegisteredUserIdCurrentEvents(userOp.get().getId(), page, pageSize);
            for (Event e : events){
                eventDTOS.add(transformDTO(e));
            }
        }

        return ResponseEntity.ok(eventDTOS);
    }

    @GetMapping("/user/registered/past")
    public ResponseEntity<List<EventDTO>> userPastRegisteredEvents(@RequestParam("page") int page, Principal principal){
        int pageSize = 3;
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (principal == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUserName(principal.getName());
        if (userOp.isPresent()){
            List<Event> events = eventService.findByRegisteredUserIdPastEvents(userOp.get().getId(), page, pageSize);
            for (Event e : events){
                eventDTOS.add(transformDTO(e));
            }
        }

        return ResponseEntity.ok(eventDTOS);
    }





    private EventDTO transformDTO(Event event) {
        if (event.getEndDate().before(new Date())) {
            return new EventFinishedDTO(event.getId(), event.getName(), event.getDescription(), event.getMaxCapacity(), event.getPrice(), event.getLocation(), event.getMap(), event.getStartDate(), event.getEndDate(), event.getAdditionalInfo(), event.getCreator().getId(), event.getNumRegisteredUsers(), event.getCategory().getId(), event.getAttendeesCount(), reviewService.calculateAverageRatingForEvent(event.getId()), reviewService.countReviewsForEvent(event.getId()));
        }
        return new EventDTO(event.getId(), event.getName(), event.getDescription(), event.getMaxCapacity(), event.getPrice(), event.getLocation(), event.getMap(), event.getStartDate(), event.getEndDate(), event.getAdditionalInfo(), event.getCreator().getId(), event.getNumRegisteredUsers(), event.getCategory().getId());
    }

    private boolean eventHasEmptyFields(EventManipulationDTO event) {
        if (
                event.getName() == null || event.getName().trim().isEmpty() ||
                event.getDescription() == null || event.getDescription().trim().isEmpty() ||
                event.getLocation() == null || event.getLocation().trim().isEmpty() ||
                event.getMap() == null || event.getMap().trim().isEmpty() ||
                event.getCategoryId() == null ||
                event.getAdditionalInfo() == null || event.getAdditionalInfo().trim().isEmpty())
        {
            return true;
        }

        if (event.getMaxCapacity() <= 0 || event.getPrice() < 0) {
            return true;
        }

        Date now = new Date();

        if (event.getStartDate() == null || event.getStartDate().before(now)) {
            return true;
        }

        if (event.getEndDate() == null || event.getEndDate().before(event.getStartDate())) {
            return true;
        }

        String mapIframeRegex = "<iframe.*src=\"https?.*\".*></iframe>";
        Pattern pattern = Pattern.compile(mapIframeRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(event.getMap());

        if (!matcher.find()) {
            return true;
        }

        return false;
    }
    private boolean isUserAdminOrCreator(String username, Event event) {
        Optional<User> userOpt = userService.findByUserName(username);
        if (!userOpt.isPresent()) {
            return false;
        }
        User user = userOpt.get();

        // Check if the user has ROLE_ADMIN authority
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Check if the user is the event creator
        boolean isCreator = event.getCreator().equals(user);

        return isAdmin || isCreator;
    }

    public Event transformEvent(EventManipulationDTO dto) {
        Event event = new Event();

        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setMaxCapacity(dto.getMaxCapacity());
        event.setPrice(dto.getPrice());
        event.setLocation(dto.getLocation());
        event.setMap(dto.getMap());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setAdditionalInfo(dto.getAdditionalInfo());
        return event;
    }
}

