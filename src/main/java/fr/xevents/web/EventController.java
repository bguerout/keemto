package fr.xevents.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.xevents.core.EventRepository;

@Controller
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventRepository repository) {
        this.eventRepository = repository;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("events", eventRepository.getAllEvents());
        return "home";
    }

}
