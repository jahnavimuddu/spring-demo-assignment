package com.springboot.thymeleaf.demo.service;

import com.springboot.thymeleaf.demo.dao.CoachDao;
import com.springboot.thymeleaf.demo.dao.CoachRepository;
import com.springboot.thymeleaf.demo.dao.PlayerRepository;
import com.springboot.thymeleaf.demo.entity.Coach;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.exceptions.CoachNotFoundException;
import com.springboot.thymeleaf.demo.user.CrmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CoachServiceImpl implements CoachService{


    private CoachDao coachDao;

    private BCryptPasswordEncoder passwordEncoder;

    private CoachRepository coachRepository;

    private PlayerRepository playerRepository;

    @Autowired
    public CoachServiceImpl(CoachRepository theCoachRepository, PlayerRepository thePlayerRepository,
                            BCryptPasswordEncoder thePasswordEncoder, CoachDao theCoachDao){
        coachRepository = theCoachRepository;
        playerRepository = thePlayerRepository;
        passwordEncoder = thePasswordEncoder;
        coachDao = theCoachDao;
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAllByOrderByGameNameAsc();
    }

    @Override
    public Coach findById(long theId) {
        Optional<Coach> result = coachRepository.findById(theId);

        Coach theCoach = null;

        if (result.isPresent()) {
            theCoach = result.get();
        }
        else {
            // we didn't find the coach
            throw new CoachNotFoundException("Did not find coach id - " + theId);
        }
        return theCoach;
    }


    @Override
    public void save(Coach theCoach) {
        theCoach.setPassword(passwordEncoder.encode(theCoach.getPassword()));
        coachRepository.save(theCoach);
    }


    @Override
    @Transactional
    public Coach findByUserName(String userName) {
        return coachDao.findCoachByName(userName);
    }

    @Override
    public List<Player> searchByGame(String theGameName) {
        List<Player> results = null;

        if (theGameName != null && (theGameName.trim().length() > 0)) {
            results = playerRepository.findByGameName(theGameName);
        }
        else {
            results = findAll();
        }
        return results;
    }

    @Transactional
    public void saveCrmUser(CrmUser crmUser) {
        Coach coach = new Coach();
        coach.setUserName(crmUser.getUserName());
        coach.setPassword(passwordEncoder.encode(crmUser.getPassword()));
        coach.setFirstName(crmUser.getFirstName());
        coach.setLastName(crmUser.getLastName());
        coach.setEmail(crmUser.getEmail());
        coach.setGameName(crmUser.getGameName());

        //save player in database
        coachRepository.save(coach);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Coach coach = coachDao.findCoachByName(userName);
        if (coach == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(coach.getUserName(), coach.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_COACH")));
    }

}
