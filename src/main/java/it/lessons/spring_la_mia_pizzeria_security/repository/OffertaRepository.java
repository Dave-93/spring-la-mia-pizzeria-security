package it.lessons.spring_la_mia_pizzeria_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.spring_la_mia_pizzeria_security.model.Offerta;

public interface OffertaRepository extends JpaRepository<Offerta, Integer>{

}