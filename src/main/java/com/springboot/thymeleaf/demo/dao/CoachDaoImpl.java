package com.springboot.thymeleaf.demo.dao;

import com.springboot.thymeleaf.demo.entity.Coach;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CoachDaoImpl implements CoachDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Coach findCoachByName(String userName) {
        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // now retrieve/read from database using username
        Query<Coach> theQuery = currentSession.createQuery("from Coach where userName=:uName", Coach.class);
        theQuery.setParameter("uName", userName);

        Coach coach = null;
        try{
            coach = theQuery.getSingleResult();
        } catch (Exception e){
            coach = null;
        }
        return coach;
    }

    @Override
    public Coach findCoachByGameName(String gameName) {
        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // now retrieve/read from database using username
        Query<Coach> theQuery = currentSession.createQuery("from Coach where gameName=:gName", Coach.class);
        theQuery.setParameter("gName", gameName);

        Coach coach = null;
        try{
            coach = theQuery.getSingleResult();
        } catch (Exception e){
            coach = null;
        }
        return coach;
    }

    @Override
    public void saveCoach(Coach coach) {
        // get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // create the user ... finally LOL
        currentSession.saveOrUpdate(coach);
    }
}
