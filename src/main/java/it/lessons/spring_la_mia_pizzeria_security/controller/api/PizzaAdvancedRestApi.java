package it.lessons.spring_la_mia_pizzeria_security.controller.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.lessons.spring_la_mia_pizzeria_security.exceptions.PizzaNotFoundException;
import it.lessons.spring_la_mia_pizzeria_security.model.Pizza;
import it.lessons.spring_la_mia_pizzeria_security.service.PizzaService;

@RestController
@RequestMapping("/api/pizzeria/v2")
public class PizzaAdvancedRestApi {

    @Autowired
    private PizzaService pizzaService;

    //Vedi tutte le pizze
    @GetMapping
    public ResponseEntity<List<Pizza>> index(@RequestParam(name="keyword", required=false)String param){
        List<Pizza> pizze = pizzaService.findAll(param);
        return new ResponseEntity<>(pizze, HttpStatus.OK);
    }
    
    //Vedi una pizza
    @GetMapping("/{id}")
    public ResponseEntity<Pizza> findById(@PathVariable Integer id){
        Optional<Pizza> optPizza = pizzaService.findById(id);
        if(optPizza.isEmpty()){
            throw new PizzaNotFoundException(id);
        }
        return new ResponseEntity<>(optPizza.get(), HttpStatus.OK);
    }
    
    //Crea una pizza
    @PostMapping
    public ResponseEntity<Pizza> create(@RequestBody Pizza entity){
        Pizza pizza = pizzaService.create(entity);
        return new ResponseEntity<>(pizza, HttpStatus.CREATED);
    }

    //Modifica una pizza
    @PutMapping("/{id}")
    public ResponseEntity<Pizza> update(@PathVariable Integer id, @RequestBody Pizza entity){
        try {
            Pizza pizzaUpd = pizzaService.update(entity);
            return new ResponseEntity<>(pizzaUpd, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Cancella una pizza
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        pizzaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}