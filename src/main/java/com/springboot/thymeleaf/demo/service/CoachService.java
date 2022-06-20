package com.springboot.thymeleaf.demo.service;

import com.springboot.thymeleaf.demo.entity.Coach;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.user.CrmUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface CoachService extends UserDetailsService {

    public Coach findByUserName(String userName);

    public Coach findById(long theId);

    public void save(CrmUser crmUser);

    public void save(Coach theCoach);

    public List<Player> findAll();

    public List<Player> searchByGame(String theGameName);

}
