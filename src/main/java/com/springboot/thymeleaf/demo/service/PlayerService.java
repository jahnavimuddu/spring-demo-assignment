package com.springboot.thymeleaf.demo.service;

import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.user.CrmUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface PlayerService extends UserDetailsService {
    public Player findByUserName(String userName);
    public Player findByGameName(String gameName);

    public Player findById(long theId);

    public void save(CrmUser crmUser);

    void saveForUpdateForm(Player thePlayer);

    public void save(Player thePlayer);

    public List<Player> findAll();

    public List<Player> searchBy(String theGameName);

    public List<Player> getUnassignedPlayers();

    public List<Player> getAssignedPlayers(Long coachId);
}
