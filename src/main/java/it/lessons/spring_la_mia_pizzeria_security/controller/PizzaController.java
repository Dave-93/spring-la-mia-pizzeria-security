package it.lessons.spring_la_mia_pizzeria_security.controller;

import java.text.NumberFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.spring_la_mia_pizzeria_security.model.Offerta;
import it.lessons.spring_la_mia_pizzeria_security.model.Pizza;
import it.lessons.spring_la_mia_pizzeria_security.repository.IngredientiRepository;
import it.lessons.spring_la_mia_pizzeria_security.service.PizzaService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/pizzeria")
public class PizzaController {

    @Autowired
    private PizzaService pizzaSer;

    @Autowired
    private IngredientiRepository ingRep;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class,
            new CustomNumberEditor(Double.class, NumberFormat.getInstance(), true));
    }

    @ModelAttribute("pizza")
    public Pizza pizzaForm() {
        return new Pizza();  // Assicurati che venga sempre inizializzato un oggetto Pizza vuoto
    }

    @GetMapping
    public String elenco(Authentication authentication, Model model, String nome) {
        model.addAttribute("list", pizzaSer.findAll(nome));
        model.addAttribute("username", authentication.getName());
        return "pizzeria/index";
    }

    /*Aggiungi*/
    @GetMapping("/addPizza")
    public String addPizza(Model model) {
        model.addAttribute("pizza", new Pizza());
        model.addAttribute("ingredientiList", ingRep.findAll());
        return "pizzeria/addPizza";
    }
    @PostMapping("/newPizza")
    public String newPizza(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            model.addAttribute("bindingResult", bindingResult);
            return "pizzeria/addPizza";
        }
        pizzaSer.create(formPizza);
        redirectAttributes.addFlashAttribute("successMessage", "Pizza creata");
        return "redirect:/pizzeria";
    }    
    /* */

    /*Dettaglio*/
    @GetMapping("/dettaglio/{id}")
    public String dettaglio(@PathVariable("id") Integer id, Model model) {
        Optional<Pizza> optPizza = pizzaSer.findById(id);
        if (optPizza.isPresent()) { 
            model.addAttribute("pizza", optPizza.get());
            model.addAttribute("ingredientiList", ingRep.findAll());
            return "/pizzeria/dettaglio";
        }
        return "redirect:/pizzeria";
    }
    /* */

    /*Modifica*/
    @GetMapping("/modifica/{id}")
    public String modifica(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("pizza", pizzaSer.findById(id).get());
        model.addAttribute("ingredientiList", ingRep.findAll());
        return "/pizzeria/modifica";
    }
    @PostMapping("/modifica/{id}")
    public String aggiornamento(@PathVariable Integer id, @Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/pizzeria/modifica";
        }
        pizzaSer.create(formPizza);
        return "redirect:/pizzeria";
    }
    /* */

    /*Cancellazione*/
    @PostMapping("/cancella/{id}")
    public String cancella(@PathVariable("id") Integer id) {
        pizzaSer.deleteById(id);
        return "redirect:/pizzeria";
    }    
    /* */

    /*Sconti*/
    @GetMapping("/{id}/sconti")
    public String sconto(@PathVariable Integer id, Model model) {
        Offerta sconto = new Offerta();
        sconto.setPizza(pizzaSer.findById(id).get());
        model.addAttribute("sconto", sconto);
        return "/offerte/addOfferta";
    }//***/
    @PostMapping("/offerte/create")
    public String creaOfferta(@Valid @ModelAttribute("sconto") Offerta formSconto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Ritorna l'oggetto sconto con gli errori al template
            model.addAttribute("bindingResult", bindingResult); 
            return "/offerte/addOfferta";  // Torna alla pagina con gli errori di validazione
        }
        pizzaSer.createOfferta(formSconto);
        redirectAttributes.addFlashAttribute("successMessage", "Sconto creato");
        return "redirect:/pizzeria/dettaglio/" + formSconto.getPizza().getId();
    }
    /* */

    /*Modifica Sconti*/
    @GetMapping("/modificaOfferta/{id}")
    public String modificaOfferta(@PathVariable("id") Integer id, Model model) {
        Optional<Offerta> optionalOfferta = pizzaSer.findOffertaById(id);
        if (optionalOfferta.isPresent()) {
            model.addAttribute("sconto", optionalOfferta.get());
            model.addAttribute("pizza", optionalOfferta.get().getPizza());
            return "offerte/modificaOfferta"; // <-- Il nome del file HTML della view
        } else {
            return "redirect:/pizzeria/dettaglio";
        }
    }
    @PostMapping("/modificaOfferta/{id}")
    public String aggiornaOfferta(@PathVariable("id") Integer id, @Valid @ModelAttribute("sconto") Offerta formSconto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "offerte/modificaOfferta";
        }
        if (formSconto.getPizza() == null || formSconto.getPizza().getId() == null) {
            // Gestisci l'errore, ad esempio con un messaggio di errore
            model.addAttribute("error", "Pizza non trovata!");
            return "offerte/modificaOfferta";
        }
        pizzaSer.createOfferta(formSconto);
        return "redirect:/pizzeria/dettaglio/" + formSconto.getPizza().getId();
    }
    /* */
}