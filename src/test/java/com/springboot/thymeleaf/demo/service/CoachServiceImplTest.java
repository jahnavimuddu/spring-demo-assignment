package com.springboot.thymeleaf.demo.service;
import com.springboot.thymeleaf.demo.dao.CoachDao;
import com.springboot.thymeleaf.demo.dao.CoachRepository;
import com.springboot.thymeleaf.demo.dao.PlayerRepository;
import com.springboot.thymeleaf.demo.entity.Coach;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.exceptions.CoachNotFoundException;
import com.springboot.thymeleaf.demo.user.CrmUser;
import java.util.Arrays;
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
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CoachServiceImplTest {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    CoachDao coachDao;

    CoachServiceImpl coachService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    List<Player> playerList = new ArrayList<>();
    List<Coach> coachList = new ArrayList<>();

    @Before
    public void beforeSetUp(){
        playerList = playerRepository.findAllByOrderByGameNameAsc();
        coachList = coachRepository.findAllByOrderByGameNameAsc();
        coachService = new CoachServiceImpl(coachRepository, playerRepository,passwordEncoder,coachDao);
    }

    @Test
    public void whenFindAllReturnsPlayersList(){
        Assert.assertEquals(playerList.size(),coachService.findAll().size());
    }

    @Test(expected = CoachNotFoundException.class)
    public void onFindByIdNotFound(){
        Coach coach = coachService.findById(999);
    }

    @Test
    public void onFindById(){
        Coach coach = coachService.findById(1);
        Assert.assertEquals("jahnavi",coach.getUserName());
    }

    @Test
    @Transactional
    public void onFindByUserName(){
        Coach coach = coachService.findByUserName("jahnavi");
        Assert.assertEquals(1,coach.getId());
    }

    @Test
    public void onSaveNewCoach(){
        int size= coachList.size();
        Coach newCoach = new Coach(size+1,"vineeth","crazy123","Vineeth","Ram","vineeth@gmail.com","Cricket");
        coachService.save(newCoach);
        Assert.assertEquals(size+1,coachRepository.findAll().size());
    }

    @Test
    public void onSearchByGameNameWhenNull(){
        int size= playerList.size();
        List<Player> players = coachService.searchByGame(null);
        Assert.assertEquals(size, players.size());
    }

   @Test
   public void onSearchByGameName(){
        int size= playerList.size();
        List<Player> players = coachService.searchByGame("Football");
        Assert.assertEquals(2, players.size());
    }

    @Test(expected = UsernameNotFoundException.class)
    @Transactional
    public void onLoadByUserNameNull(){
        coachService.loadUserByUsername(null);
    }

    @Test
    @Transactional
    public void onLoadByUserNameNotNull(){
        Assert.assertEquals(Arrays.asList(new SimpleGrantedAuthority("ROLE_COACH")),
                coachService.loadUserByUsername("jahnavi").getAuthorities().stream().collect(Collectors.toList()));
    }

    @Test
    @Transactional
    public void onSaveCrmUser(){
        CrmUser crmUser = new CrmUser();
        crmUser.setUserName("ram");
        crmUser.setPassword("crazy123");
        crmUser.setMatchingPassword("crazy123");
        crmUser.setFirstName("Ram");
        crmUser.setLastName("Kumar");
        crmUser.setEmail("ram@gmail.com");
        crmUser.setGameName("Hockey");
        crmUser.setUserType("Coach");
        coachService.saveCrmUser(crmUser);
    }
}

