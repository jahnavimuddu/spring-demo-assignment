package com.springboot.thymeleaf.demo.dao;

import com.springboot.thymeleaf.demo.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    // add a method to sort by game name
    public List<Player> findAllByOrderByGameNameAsc();

    // search by name
    public List<Player> findByFirstNameContainsOrLastNameContainsAllIgnoreCase(String name, String lName);

    public List<Player> findByGameName(String theGameName);

    public List<Player> findByCoachId(Long theCoachId);
}
