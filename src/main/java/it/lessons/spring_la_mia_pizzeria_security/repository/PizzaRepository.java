package it.lessons.spring_la_mia_pizzeria_security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.spring_la_mia_pizzeria_security.model.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Integer>{

    public List<Pizza>findByNomeContainingIgnoreCase(String nome);

}