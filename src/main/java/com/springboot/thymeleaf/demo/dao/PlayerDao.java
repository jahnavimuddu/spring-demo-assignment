package com.springboot.thymeleaf.demo.dao;

import com.springboot.thymeleaf.demo.entity.Player;

public interface PlayerDao {

    public Player findPlayerByName(String userName);
    public Player findPlayerByGameName(String gameName);

    public void savePlayer(Player player);

}
