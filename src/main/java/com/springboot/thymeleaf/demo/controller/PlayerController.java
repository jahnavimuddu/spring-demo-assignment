package com.springboot.thymeleaf.demo.controller;

import com.springboot.thymeleaf.demo.dto.PlayerDto;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/playerLanding")
public class PlayerController {

    private static final String PLAYERS = "players";
    private static final String LIST_PLAYERS = "list-players";
    private PlayerService playerService;

    public PlayerController(PlayerService thePlayerService) {
        playerService = thePlayerService;
    }

    // add mapping for "/list"
    @GetMapping(value={"","/list"})
    public String listPlayers(Model theModel) {

        // get players from db
        List<Player> thePlayers = playerService.findAll();

        // add to the spring model
        theModel.addAttribute(PLAYERS, thePlayers);


        return LIST_PLAYERS;
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("playerId") long theId,
                                    Model theModel) {

        // get the player from the service
        Player thePlayer = playerService.findById(theId);

        // set player as a model attribute to pre-populate the form
        theModel.addAttribute("player", thePlayer);

        // send over to our form
        return "player-update-form";
    }

    @PostMapping("/save")
    public String savePlayer(@ModelAttribute("player") PlayerDto thePlayer) {
        Player player = new Player(thePlayer.getId(), thePlayer.getUserName(), thePlayer.getPassword(), thePlayer.getFirstName(), thePlayer.getLastName(), thePlayer.getEmail(), thePlayer.getGameName());
        player.setCoach(thePlayer.getCoach());
        // save the player
        playerService.saveForUpdateForm(player);

        // use a redirect to prevent duplicate submissions
        return "redirect:/playerLanding/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam("playerName") String theName,
                         Model theModel) {

        // delete the player
        List<Player> thePlayers = playerService.searchBy(theName);

        // add to the spring model
        theModel.addAttribute(PLAYERS, thePlayers);

        // send to /playerLanding/list
        return LIST_PLAYERS;

    }

}
