package it.lessons.spring_la_mia_pizzeria_security.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.lessons.spring_la_mia_pizzeria_security.model.Pizza;
import it.lessons.spring_la_mia_pizzeria_security.service.PizzaService;

@RestController
@RequestMapping("/api/pizzeria")
public class PizzaRestController {

    @Autowired
    private PizzaService pizzaService;

    //Vedi tutte le pizze
    @GetMapping
    public List<Pizza> index(@RequestParam(name="keyword", required=false)String keyword) {
        return pizzaService.findAll(keyword);
    }
    
    //Crea una pizza
    @PostMapping
    public Pizza create(@RequestBody Pizza pizza) {
        pizza = pizzaService.create(pizza);        
        return pizza;
    }
    
    //Modifica una pizza
    @PutMapping("/{id}")
    public Pizza modify(@PathVariable Integer id, @RequestBody Pizza pizza) {        
        pizza = pizzaService.update(pizza);
        return pizza;
    }

    //Elimina una pizza
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        pizzaService.deleteById(id);
    }
}