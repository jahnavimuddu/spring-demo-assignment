package com.springboot.thymeleaf.demo.dao;

import com.springboot.thymeleaf.demo.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    // add a method to sort by game name
    public List<Coach> findAllByOrderByGameNameAsc();

    // search by name
    public List<Coach> findByFirstNameContainsOrLastNameContainsAllIgnoreCase(String name, String lName);
}