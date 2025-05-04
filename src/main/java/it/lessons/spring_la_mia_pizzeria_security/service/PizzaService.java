package it.lessons.spring_la_mia_pizzeria_security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.lessons.spring_la_mia_pizzeria_security.model.Offerta;
import it.lessons.spring_la_mia_pizzeria_security.model.Pizza;
import it.lessons.spring_la_mia_pizzeria_security.repository.OffertaRepository;
import it.lessons.spring_la_mia_pizzeria_security.repository.PizzaRepository;

@Service
public class PizzaService {

    private PizzaRepository pizzaRepository;
    
    private OffertaRepository offertaRepository;

    //private IngredientiRepository ingredientiRepository;
    
    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, OffertaRepository offertaRepository/*, IngredientiRepository ingredientiRepository*/){
        this.pizzaRepository = pizzaRepository;
        this.offertaRepository = offertaRepository;
       // this.ingredientiRepository = ingredientiRepository;
    }

    //trova tutte le pizze
    public List<Pizza> findAll(String nome){
        List<Pizza> list;
        if(nome != null && !nome.isBlank()){
            list = pizzaRepository.findByNomeContainingIgnoreCase(nome);
        }else{
            list = pizzaRepository.findAll();
        }
        return list;
    }

    //trova la pizza per id
    public Optional<Pizza> findById(Integer id){
        return pizzaRepository.findById(id);
    }

    //crea pizza
    public Pizza create(Pizza pizza){
        return pizzaRepository.save(pizza);
    }

    //modifica pizza
    public Pizza update(Pizza pizza){
        Optional<Pizza> optPiz = pizzaRepository.findById(pizza.getId());
        if(optPiz.isEmpty()){
            throw new IllegalArgumentException("Senza id non puoi modificare la pizza!");
        }
        return pizzaRepository.save(pizza);
    }

    //cancella pizza e offerte collegate
    public void deleteById(Integer id){
        Optional<Pizza> pizzaId = pizzaRepository.findById(id);
        if(pizzaId.isPresent()){
            Pizza pizza =pizzaId.get();
            for(Offerta of : pizza.getSconti()){
                offertaRepository.deleteById(of.getId());
            }
            pizzaRepository.deleteById(id);
        }
    }

    //crea offerta
    public Offerta createOfferta(Offerta offerta){
        return offertaRepository.save(offerta);
    }

    //trova offerta per id
    public Optional<Offerta> findOffertaById(Integer id){
        return offertaRepository.findById(id);
    }
}