package fr.xevents.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope(value = "request")
public class UserConnectionController {

    private final ConnectionRepository connectionRepository;

    @Autowired
    public UserConnectionController(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping("/connections")
    public String getAllUserConnections(Model model) {
        model.addAttribute("connections", connectionRepository.findAllConnections());
        return "connections";
    }
}
