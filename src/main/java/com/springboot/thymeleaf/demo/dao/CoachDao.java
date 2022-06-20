package com.springboot.thymeleaf.demo.dao;

import com.springboot.thymeleaf.demo.entity.Coach;

public interface CoachDao {

    public Coach findCoachByName(String userName);
    public Coach findCoachByGameName(String gameName);
    public void saveCoach(Coach coach);

}
