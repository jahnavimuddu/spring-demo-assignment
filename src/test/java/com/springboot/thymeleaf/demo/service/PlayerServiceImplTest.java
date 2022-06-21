package com.springboot.thymeleaf.demo.service;

import com.springboot.thymeleaf.demo.dao.PlayerDao;
import com.springboot.thymeleaf.demo.dao.PlayerRepository;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.exceptions.PlayerNotFoundException;
import com.springboot.thymeleaf.demo.user.CrmUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceImplTest {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    PlayerServiceImpl playerService;

    List<Player> playerList = new ArrayList<>();

    @Before
    public void beforeSetUp(){
        playerList = playerRepository.findAllByOrderByGameNameAsc();
        playerService = new PlayerServiceImpl(playerRepository,passwordEncoder,playerDao);
    }

    @Test
    public void whenFindAllReturnsPlayersList(){
        Assert.assertEquals(playerList.size(),playerService.findAll().size());
    }

    @Test(expected = PlayerNotFoundException.class)
    public void onFindByIdNotFound(){
        Player player = playerService.findById(999);
    }

    @Test
    public void onFindById(){
        Player player = playerService.findById(1);
        Assert.assertEquals("sreeja",player.getUserName());
    }

    @Test
    public void onSearchByNull(){
        List<Player> players = playerService.searchBy(null);
        Assert.assertEquals(playerList.size(),players.size());
    }

    @Test
    public void onSearchByNotFound(){
        List<Player> players = playerService.searchBy("  ");
        Assert.assertEquals(playerList.size(),players.size());
    }

    @Test
    public void onSearchBy(){
        List<Player> players = playerService.searchBy("Sreeja");
        Assert.assertEquals(1,players.get(0).getId());
    }

    @Test
    @Transactional
    public void onFindByUserName(){
        Player player = playerService.findByUserName("sreeja");
        Assert.assertEquals(1,player.getId());
    }

    @Test
    public void onSaveNewPlayer(){
        int size= playerList.size();
        Player newPlayer = new Player(size+1,"susan","crazy123","Susan","Williams","susan@gmail.com","Cricket");
        playerService.saveCrmUser(newPlayer);
        Assert.assertEquals(size+1,playerRepository.findAll().size());
    }

    @Test
    @Transactional
    public void onFindByGameName(){
        Player player = playerService.findByGameName("Tennis");
        Assert.assertEquals("trupti", player.getUserName());
    }

    @Test
    public void whenGetUnassignedPlayers(){
        List<Player> unassignedPlayers = playerService.getUnassignedPlayers();
        Assert.assertEquals("John",unassignedPlayers.get(0).getUserName());
    }

    @Test
    public void whenGetAssignedPlayers(){
        List<Player> assignedPlayers = playerService.getAssignedPlayers(Long.valueOf(1));
        Assert.assertEquals("trupti",assignedPlayers.get(0).getUserName());
    }

    @Test(expected = UsernameNotFoundException.class)
    @Transactional
    public void onLoadByUserNameNull(){
        playerService.loadUserByUsername(null);
    }

    @Test
    @Transactional
    public void onLoadByUserNameNotNull(){
        Assert.assertEquals(Arrays.asList(new SimpleGrantedAuthority("ROLE_PLAYER")),
                playerService.loadUserByUsername("sreeja").getAuthorities().stream().collect(Collectors.toList()));
    }

    @Test
    @Transactional
    public void onSaveCrmUser(){
        CrmUser crmUser = new CrmUser();
        crmUser.setUserName("sai");
        crmUser.setPassword("crazy123");
        crmUser.setMatchingPassword("crazy123");
        crmUser.setFirstName("sai");
        crmUser.setLastName("Kumar");
        crmUser.setEmail("sai@gmail.com");
        crmUser.setGameName("Hockey");
        crmUser.setUserType("Player");
        playerService.saveCrmUser(crmUser);
        Assert.assertEquals("Player",crmUser.getUserType());
    }

    @Test
    public void onSaveForUpdateForm(){
        Player player = new Player(2,"John","crazy123","John","Williams","john@gmail.com","Football");
        playerService.saveForUpdateForm(player);
        Assert.assertEquals(player.getEmail(),playerRepository.findById(Long.valueOf(2)).get().getEmail());
    }

}
