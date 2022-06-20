package com.springboot.thymeleaf.demo.dao;

import com.springboot.thymeleaf.demo.entity.Player;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class PlayerDaoImpl implements PlayerDao{

    @Autowired
    private EntityManager entityManager;

    @Override
    public Player findPlayerByName(String userName) {

        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // now retrieve/read from database using username
        Query<Player> theQuery = currentSession.createQuery("from Player where userName=:uName", Player.class);
        theQuery.setParameter("uName", userName);

        Player player = null;
        try{
            player = theQuery.getSingleResult();
        } catch (Exception e){
            player = null;
        }
        return player;
    }

    @Override
    public Player findPlayerByGameName(String gameName) {
        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // now retrieve/read from database using username
        Query<Player> theQuery = currentSession.createQuery("from Player where gameName=:gName", Player.class);
        theQuery.setParameter("gName", gameName);

        Player player = null;
        try{
            player = theQuery.getSingleResult();
        } catch (Exception e){
            player = null;
        }
        return player;
    }

    @Override
    public void savePlayer(Player player) {
        // get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // create the user ... finally LOL
        currentSession.saveOrUpdate(player);
    }
}
