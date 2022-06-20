package com.springboot.thymeleaf.demo.controller;


import com.springboot.thymeleaf.demo.dto.CoachDto;
import com.springboot.thymeleaf.demo.entity.Coach;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.service.CoachService;
import com.springboot.thymeleaf.demo.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coachLanding")
public class CoachController {

    private static final String PLAYERS = "players";

    private static final String LIST_COACHES = "list-coaches";

    private CoachService coachService;

    private PlayerService playerService;

    public CoachController(CoachService theCoachService,PlayerService thePlayerService) {
        coachService = theCoachService;
        playerService = thePlayerService;
    }

    // add mapping for "/list"
    @GetMapping(value={"","/list"})
    public String listPlayers(Model theModel) {

        // get coaches from db
        List<Player> thePlayers = playerService.findAll();

        // add to the spring model
        theModel.addAttribute(PLAYERS, thePlayers);

        return LIST_COACHES;
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("coachId") long theId,
                                    Model theModel) {

        // get the coach from the service
        Coach theCoach = coachService.findById(theId);

        // set coach as a model attribute to pre-populate the form
        theModel.addAttribute("coach", theCoach);

        // send over to our form
        return "coach-update-form";
    }

    @PostMapping("/save")
    public String saveCoach(@ModelAttribute("coach") CoachDto theCoach) {
        Coach coach= new Coach(theCoach.getId(),theCoach.getUserName(), theCoach.getPassword(), theCoach.getFirstName(), theCoach.getLastName(), theCoach.getEmail(), theCoach.getGameName());
        coach.setPlayers(theCoach.getPlayers());
        // save the coach
        coachService.save(coach);

        // use a redirect to prevent duplicate submissions
        return "redirect:/coachLanding/list";
    }

    @GetMapping("/searchByName")
    public String searchByName(@RequestParam("playerName") String theName,
                         Model theModel) {

        // delete the coach
        List<Player> thePlayers = playerService.searchBy(theName);

        // add to the spring model
        theModel.addAttribute(PLAYERS, thePlayers);

        // send to /coachLanding/list
        return LIST_COACHES;

    }

    @GetMapping("/searchByGame")
    public String searchByGame(@RequestParam("gameName") String theGameName,
                               Model theModel) {

        // delete the coach
        List<Player> thePlayers = coachService.searchByGame(theGameName);

        // add to the spring model
        theModel.addAttribute(PLAYERS, thePlayers);

        // send to /coachLanding/list
        return LIST_COACHES;

    }

    @GetMapping("/viewMyPlayers")
    public String viewMyPlayers(@RequestParam("coachId") long theId,
                                Model theModel){

        theModel.addAttribute("myPlayers" , coachService.findById(theId).getPlayers());

        List<Player> otherPlayers = playerService.getUnassignedPlayers();
        theModel.addAttribute("otherPlayers",otherPlayers);

        return "my-players";
    }

    @GetMapping("/assignPlayer")
    public String assignPlayer(@RequestParam("playerId") long theId,
                               @RequestParam("coachId") long coachId,
                               Model theModel){
        Coach theCoach = coachService.findById(coachId);
        Player player = playerService.findById(theId);
        theCoach.add(player);
        playerService.save(player);
        return "redirect:/coachLanding/viewMyPlayers?coachId=" + theCoach.getId() ;
    }

    @GetMapping("/removePlayer")
    public String removePlayer(@RequestParam("playerId") long theId,
                               @RequestParam("coachId") long coachId,
                               Model theModel){
        Coach theCoach = coachService.findById(coachId);
        Player player = playerService.findById(theId);
        theCoach.remove(player);
        playerService.save(player);
        return "redirect:/coachLanding/viewMyPlayers?coachId=" + theCoach.getId() ;
    }


}
