package com.springboot.thymeleaf.demo.service;

import com.springboot.thymeleaf.demo.dao.PlayerDao;
import com.springboot.thymeleaf.demo.dao.PlayerRepository;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.exceptions.PlayerNotFoundException;
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
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository thePlayerRepository,BCryptPasswordEncoder thePasswordEncoder, PlayerDao thePlayerDao){
        playerRepository = thePlayerRepository;
        passwordEncoder = thePasswordEncoder;
        playerDao = thePlayerDao;
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAllByOrderByGameNameAsc();
    }

    @Override
    public Player findById(long theId) {
        Optional<Player> result = playerRepository.findById(theId);

        Player thePlayer = null;

        if (result.isPresent()) {
            thePlayer = result.get();
        }
        else {
            // we didn't find the employee
            throw new PlayerNotFoundException("Did not find player id - " + theId);
        }
        return thePlayer;
    }

    @Override
    public void saveForUpdateForm(Player thePlayer) {
        thePlayer.setPassword(passwordEncoder.encode(thePlayer.getPassword()));
        playerRepository.save(thePlayer);
    }

    @Override
    public void saveCrmUser(Player thePlayer) {
        playerRepository.save(thePlayer);
    }


    @Override
    public List<Player> searchBy(String theName) {

        List<Player> results = null;

        if (theName != null && (theName.trim().length() > 0)) {
            results = playerRepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(theName, theName);
        }
        else {
            results = findAll();
        }

        return results;
    }

    @Override
    public List<Player> getUnassignedPlayers() {
        return playerRepository.findByCoachId(null);
    }

    @Override
    public List<Player> getAssignedPlayers(Long coachId) {
        return playerRepository.findByCoachId(coachId);
    }

    @Override
    @Transactional
    public Player findByUserName(String userName) {
        // check the database if the user already exists
        return playerDao.findPlayerByName(userName);
    }

    @Override
    @Transactional
    public Player findByGameName(String gameName) {
        // check the database if the user already exists
        return playerDao.findPlayerByGameName(gameName);
    }

    @Override
    @Transactional
    public void saveCrmUser(CrmUser crmUser) {
        Player player = new Player();
        player.setUserName(crmUser.getUserName());
        player.setPassword(passwordEncoder.encode(crmUser.getPassword()));
        player.setFirstName(crmUser.getFirstName());
        player.setLastName(crmUser.getLastName());
        player.setEmail(crmUser.getEmail());
        player.setGameName(crmUser.getGameName());

        //save player in database
        playerDao.savePlayer(player);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Player player = playerDao.findPlayerByName(userName);
        if (player == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(player.getUserName(), player.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_PLAYER")));
    }

}
