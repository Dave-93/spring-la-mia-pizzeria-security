package it.lessons.spring_la_mia_pizzeria_security.exceptions;

public class PizzaNotFoundException extends RuntimeException{

    public PizzaNotFoundException(Integer id){
        super("Non esiste nessuna pizza con id " + id);
    }

}